package com.alan.moeliatest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alan.moeliatest.adapters.MyRecyclerAdapter;
import com.alan.moeliatest.managers.SharedPreferencesController;
import com.alan.moeliatest.managers.WSConnectionManager;
import com.alan.moeliatest.models.Movie;
import com.alan.moeliatest.utils.CallbackMethod;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * In this Activity we'll display a list of Movies. Whenever we add a new movie, it will be stored
 * by converting the list of movies into a JSON object, allowing us to recover our previously stored
 * movies whenever we return to this app
 */
public class MainListActivity extends AppCompatActivity {

    private FloatingActionButton addMovieButton;
    private RecyclerView movieRecyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private List<Movie> myMovies;
    private Button btnCancel;
    private Dialog addDialog;
    private EditText txtMovie;
    private TextView tvWarning;
    private View dialogView;
    private Button btnAddMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_list_layout);

        //We load our references

        loadReferences();

    }

    private void loadReferences() {

        addMovieButton = (FloatingActionButton)findViewById(R.id.add_movie_button);

        //Whenever the user clicks the floating button it will call the requestMovieTitle method
        //that will show the input dialog for the user to enter the desired movie.
        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMovieTitle();
            }
        });

        movieRecyclerView = (RecyclerView)findViewById(R.id.list_recycler_view);

        //Now that we have our recyclerview, we'll load our previously loaded movies or create
        //an empty list in case we don't have any stored movies
        loadSavedData();

        //With our movie list regenerated, it's safe to load our adapter
        myRecyclerAdapter = new MyRecyclerAdapter(myMovies,this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        movieRecyclerView.setLayoutManager(linearLayoutManager);

        movieRecyclerView.setAdapter(myRecyclerAdapter);
    }


    private void loadSavedData() {

        myMovies = SharedPreferencesController.getSavedMovies(this);

    }


    private void requestMovieTitle() {

        //If we already have an instance of the createAddDialog object, we'll use that dialog
        //after clearing it's fields, if not, we'll create a new instance by calling the createAddDialog method
        if(addDialog == null)
            createAddDialog();
        else{
            tvWarning.setVisibility(View.GONE);
            txtMovie.setText("");
        }
        addDialog.show();

    }

    private void createAddDialog(){
        addDialog = new Dialog(this);
        addDialog.setTitle(R.string.add_movie_dialog_title);

        dialogView = View.inflate(this,R.layout.movie_dialog_layout,null);
        txtMovie = (EditText)dialogView.findViewById(R.id.txtMovieTitle);
        tvWarning = (TextView)dialogView.findViewById(R.id.tvWarning);
        tvWarning.setVisibility(View.GONE);

        addDialog.setCancelable(false);

        txtMovie.setInputType(InputType.TYPE_CLASS_TEXT);

        addDialog.setContentView(dialogView);

        btnCancel = (Button)dialogView.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });

        btnAddMovie = (Button)dialogView.findViewById(R.id.btnAdd);

        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If the user clicks on the add button, we'll make sure it has a valid title first
                if (txtMovie.getText().toString().equals("")) {

                    //Since it's empty, we'll show the corresponding warning
                    tvWarning.setVisibility(View.VISIBLE);

                } else {

                    //Since the text is valid, we'll dismiss the dialog and fetch the results by calling the WSConnectionManager's performAction method
                    addDialog.dismiss();

                    String url = getString(R.string.movie_fetch_url).replace("[TITLE]", txtMovie.getText().toString());

                    //When we call this method, we need to pass an instance of CallbackMethod overriding it's methods
                    WSConnectionManager.performAction(url, new CallbackMethod() {
                        @Override
                        public void success(String response) {

                            //When the operation is a success, we'll create a Movie out of the JSON response
                            Gson gson = new Gson();

                            Type type = new TypeToken<List<Movie>>() {
                            }.getType();

                            //In case the response is empty, we'll display a dialog notifying this issue
                            if (response.equals("")) {
                                showNoResultsDialog();
                            } else {

                                try {

                                    List<Movie> movies = (List<Movie>) gson.fromJson(response, type);

                                    //When we successfully create a list of movies we'll add those to the current list
                                    //of movies, and then store them by calling the storeMovies method.

                                    movies.addAll(myMovies);
                                    myMovies.clear();
                                    myMovies.addAll(movies);

                                    SharedPreferencesController.storeMovies(myMovies, getBaseContext());

                                    myRecyclerAdapter.notifyDataSetChanged();

                                } catch (JsonSyntaxException ex) {

                                    ex.printStackTrace();

                                    //In case of parse error we'll alert the user that no results were found
                                    showNoResultsDialog();

                                }
                            }
                        }

                        @Override
                        public void failed(String response) {

                            //In case of connection error we'll alert the user that no results were found
                            showNoResultsDialog();
                        }
                    }, MainListActivity.this);
                }
            }
        });
    }

    /**
     * This method creates an alert dialog that will notify the user that no results were found,
     * and will also give the user the option to enter a new title without having to click on the
     * floating button again.
     */
    private void showNoResultsDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle(getString(R.string.no_results_dialog_title));

        dialogBuilder.setMessage(getString(R.string.no_results_dialog_message));

        dialogBuilder.setPositiveButton(getString(R.string.no_results_dialog_add_another), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                requestMovieTitle();
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.no_results_dialog_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        dialogBuilder.create().show();
    }

}
