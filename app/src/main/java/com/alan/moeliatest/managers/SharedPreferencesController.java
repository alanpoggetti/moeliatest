package com.alan.moeliatest.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.alan.moeliatest.R;
import com.alan.moeliatest.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple class that will manage stored preferences.
 */
public class SharedPreferencesController {

    private static SharedPreferences prefs;
    private static Context mContext;

    public static SharedPreferences getPrefs(Context context){

        if(prefs == null){

            mContext = context;

            prefs = context.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);

        }

        return prefs;

    }

    /**
     * This method will recover all stored movies. The movies are stored in JSON format and then
     * converted into a List of Movie instances.
     *
     * @param context
     * @return List of movies
     */

    public static List<Movie> getSavedMovies(Context context){

        if(prefs == null)
            prefs = getPrefs(context);

        String storedMovies = prefs.getString("stored_movies","");

        Gson gson = new Gson();

        Type type = new TypeToken<List<Movie>>() {}.getType();

        List<Movie> movies = (List<Movie>) gson.fromJson(storedMovies, type);

        if(movies == null)
            movies = new ArrayList<>();

        return movies;

    }

    /**
     * This method will store a list of movie instances by converting them into JSON
     * @param myMovies List of movies to be stored
     * @param context
     */
    public static void storeMovies(List<Movie> myMovies, Context context){

        if(prefs == null)
            prefs = getPrefs(context);

        Gson gson = new Gson();

        String moviesJson = gson.toJson(myMovies);

        prefs.edit().putString("stored_movies",moviesJson).apply();

    }
}
