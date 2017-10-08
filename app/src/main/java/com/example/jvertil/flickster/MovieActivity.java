package com.example.jvertil.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jvertil.flickster.adapters.MovieArrayAdapter;
import com.example.jvertil.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    int movieId; // needed for the video API
    String movieKey; // needed for the video API
    public String mKey;
    public JSONArray movieVideoJsonResults = null;
    public boolean verifier = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Figuring out orientation
        int orientation = getResources().getConfiguration().orientation;

        // Handling views
        lvItems = (ListView) findViewById(R.id.lvMovies);
        // Creating an empty ArrayList to store the movie objects.
        movies = new ArrayList<>();
        // Instantiating the movie adapter
        movieAdapter = new MovieArrayAdapter(this, movies, orientation); // orientation is needed for this particular app as landscape and portrait mode are dealt with differently.
        // Connecting the arrayAdapter to the listView
        lvItems.setAdapter(movieAdapter);

        // Dealing with networking to get online data in JSON format
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        AsyncHttpClient client = new AsyncHttpClient();


        client.get(url, new JsonHttpResponseHandler(){ // bc our response will be in JSON format.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults= response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArry(movieJsonResults)); // addAll is important here. we put all the JSON objects into the movies arrayList
                    // We need to notify the adapter of the change in order for the data to get displayed
                    movieAdapter.notifyDataSetChanged();
                    // Adding a log statement for debuging purposes
                    Log.d("DEBUG", movieJsonResults.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        //Setting onClickListener to list view items
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie selectedObject = movies.get(position);
                while(selectedObject == null){}
                movieId = selectedObject.getMovieId();


                Intent intent = new Intent(MovieActivity.this, DetailedActivity.class);
                // We need to pass the appropriate Movie JSON object from this (MovieActivity) to the DetailedActivity
                intent.putExtra("data", selectedObject);
                // Pass in the movieId
                intent.putExtra("movId", movieId);

                startActivity(intent);
            }
        });


    }

}
