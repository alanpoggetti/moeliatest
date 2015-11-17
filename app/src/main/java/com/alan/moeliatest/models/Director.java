package com.alan.moeliatest.models;

import com.google.gson.annotations.SerializedName;

/**
 * This is Director class is used by the Movie class.
 */
public class Director {
    @SerializedName("name")
    private String director;

    public Director(String director) {
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
