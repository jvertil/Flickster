package com.example.jvertil.flickster;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jvertil.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jvertil on 9/8/17.
 */

public class DetailedActivity extends YouTubeBaseActivity{

    public String MOVIEKEY;
    int orientation =1;
    public JSONArray movieVideoJsonResults = null;
    String mKey;
    int movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_page);

        // Figuring out orientation
        orientation = getResources().getConfiguration().orientation;


        Intent intent = getIntent();
        Movie theSelectedMovieObj = intent.getParcelableExtra("data");
        movieId = intent.getIntExtra("movId", 0); // default value..


                try {
                    updateMovieKey();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        // Setting the appropriate data:
        String mTitle = theSelectedMovieObj.getOrginalTitle();
        String mReleaseDate = theSelectedMovieObj.getReleaseDate();
        String mSummary = theSelectedMovieObj.getOverview();
        float mRating = (float) theSelectedMovieObj.getStarRating();
        int mId = theSelectedMovieObj.getMovieId();

        // Declaring  the appropriate views
        TextView movieTitle = (TextView) findViewById(R.id.movietitle);
        TextView releaseDate = (TextView) findViewById(R.id.releasedate);
        TextView movieSummary = (TextView) findViewById(R.id.summary);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Button returnButton = (Button) findViewById(R.id.returnButton);

        // Populating the views with the appropriate data
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieTitle.setText(mTitle);
            releaseDate.setText(mReleaseDate);
            movieSummary.setText(mSummary);
            ratingBar.setRating(mRating);

            // Adding onClickListener on the button to return to the previous/main activity page
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();


                }
            });

        }



        // Setting up Youtube
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeView);

        youTubePlayerView.initialize("AIzaSyDXxacRrxPXuJj8ptlz2NTGKSwnHW67aPM", // this string of character is the YouTube API Key
                new YouTubePlayer.OnInitializedListener(){
                   // If you wish to only load the video but not play, use cueVideo() instead of loadVideo().
                    // Playing videos involves passing along the YouTube video key (do not include the full URL):

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                            smallDelay();
                            while (MOVIEKEY == null){}
                            youTubePlayer.cueVideo(MOVIEKEY);
                            MOVIEKEY = null;
                        }

                        else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                            smallDelay();
                            while (MOVIEKEY == null){}
                            youTubePlayer.loadVideo(MOVIEKEY); // in landscape mode, the video gets loaded without the user needing to press the play button
                            MOVIEKEY = null;
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

    }

    public void updateMovieKey() throws JSONException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlVideo = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url(urlVideo).build();


                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    JSONObject mJsonObect = new JSONObject(result);

                    if(response.isSuccessful()){
                        JSONArray mJsonArray = mJsonObect.getJSONArray("results");
                        movieVideoJsonResults = mJsonArray;
                        MOVIEKEY = movieVideoJsonResults.getJSONObject(0).getString("key");
                        while (MOVIEKEY == null){}

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    // To enable a 1 second delay
    public static void smallDelay(){
        long endTime = System.currentTimeMillis() + 1*1000; // there's a thousand milliseconds in a second. adding 10,000 millsecs is adding 10 secs to the current time

        while (System.currentTimeMillis() < endTime){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

