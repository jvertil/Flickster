package com.example.jvertil.flickster.models;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jvertil on 10/3/17.
 */

public class MovieKeyHolder {

    public String mKey;
    public JSONArray movieVideoJsonResults = null;
    public int movId;

    // Make a constructor for this class


    public MovieKeyHolder(int movId) {
        this.movId = movId;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String key) {
        this.mKey = key;
    }



    public void updateMovieKey(int movId) throws JSONException {

        String urlVideo = "https://api.themoviedb.org/3/movie/" + movId + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(urlVideo, new JsonHttpResponseHandler() {
            @Override


            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    movieVideoJsonResults= response.getJSONArray("results");
                    // Adding a log statement for debuging purposes
                    Log.d("DEBUG", movieVideoJsonResults.toString());
                    mKey = movieVideoJsonResults.getJSONObject(0).getString("key"); // for this particular API and functionality, we only needed the first object.
                    // now set this movieKey equal to the "key" being returned



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        });


    }
}
