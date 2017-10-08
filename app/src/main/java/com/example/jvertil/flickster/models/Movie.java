package com.example.jvertil.flickster.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jvertil on 9/2/17.
 */

public class Movie implements Parcelable { // the parcelable implementation is needed to enable the object serialization when passing data among different activities

    // Generating getters
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath); // %s is an indication that the poster path should be inserted in that location
    }

    public String getOrginalTitle() {
        return orginalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w600/%s",backdropPath); // %s is an indication that the backdrop path should be inserted in that location;
    }

    public String getReleaseDate() {
        return String.format("Release date: %s",releaseDate);
    }

    public double getStarRating() {
        return (starRating/2);
    }

    public int getMovieId() {
        return movieId;
    }

    String posterPath;
    String orginalTitle;
    String overview;
    String backdropPath;
    String releaseDate;
    double starRating;
    int movieId;

    // Now we create constructors to take the JSON objects and populate the appropriate fields
    public Movie(JSONObject jsonObject) throws JSONException { // must include throws JSONException..
        this.posterPath = jsonObject.getString("poster_path");
        this.orginalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.releaseDate = jsonObject.getString("release_date");
        this.starRating = jsonObject.getDouble("vote_average");
        this.movieId = jsonObject.getInt("id");

    }


    // Adding all the JSON objects into an ArrayList
    public static ArrayList<Movie> fromJSONArry(JSONArray array){ // Note that an ArrayList can grow or shrink
        ArrayList<Movie> results = new ArrayList<>();

        for (int x=0; x < array.length(); x++){
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return results;
    }

    // Again, parcelable is implemented for easy passing of objects between acitivities

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeString(this.orginalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.backdropPath);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.starRating);
        dest.writeInt(this.movieId);
    }

    protected Movie(Parcel in) {
        this.posterPath = in.readString();
        this.orginalTitle = in.readString();
        this.overview = in.readString();
        this.backdropPath = in.readString();
        this.releaseDate = in.readString();
        this.starRating = in.readDouble();
        this.movieId = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
