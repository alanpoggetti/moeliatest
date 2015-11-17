package com.alan.moeliatest.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alan.moeliatest.R;
import com.alan.moeliatest.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This manager will configure each card with the provided Movie data
 */
public class MyRecyclerAdapter  extends RecyclerView.Adapter<MyRecyclerAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context mContext;

    public MyRecyclerAdapter(List<Movie> movieList,Context context){
        this.movieList = movieList;
        this.mContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        final MovieViewHolder mvh = new MovieViewHolder(v);

        mvh.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mvh.description.getText().toString().equals("")) {
                    if (mvh.description.getVisibility() == View.VISIBLE)
                        mvh.description.setVisibility(View.GONE);
                    else
                        mvh.description.setVisibility(View.VISIBLE);
                }
            }
        });
        return mvh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.director.setText(movie.getDirector());
        holder.year.setText(movie.getYear());
        holder.description.setText(movie.getPlot());

        if(!movie.getImageUrl().equals(""))
            Picasso.with(mContext).load(movie.getImageUrl()).into(holder.moviePhoto);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView director;
        TextView title;
        TextView year;
        TextView description;
        ImageView moviePhoto;

        MovieViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            director = (TextView)itemView.findViewById(R.id.director);
            title = (TextView)itemView.findViewById(R.id.movie_name);
            year = (TextView)itemView.findViewById(R.id.year);
            moviePhoto = (ImageView)itemView.findViewById(R.id.movie_photo);
            description = (TextView)itemView.findViewById(R.id.movie_description);
        }
    }
}
