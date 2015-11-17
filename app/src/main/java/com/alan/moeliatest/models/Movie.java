package com.alan.moeliatest.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This is our movie model
 */
public class Movie {

    @SerializedName("urlPoster")
    private String imageUrl;
    @SerializedName("title")
    private String title;
    @SerializedName("directors")
    private List<Director> directors;
    private String director;
    @SerializedName("year")
    private String year;
    @SerializedName("plot")
    private String plot;

    public Movie(String imageUrl, String title, List<Director> directors, String year, String plot) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.directors = directors;
        this.year = year;
        this.plot = plot;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {

        if(directors.size()>0)
            director = directors.get(0).getDirector();
        else
            director = "";
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }
}
