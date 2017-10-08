package com.example.jvertil.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jvertil.flickster.R;
import com.example.jvertil.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jvertil on 9/3/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie>   { // Note the statement to include extends ArrayAdapter with the appropriate class

    public int orient;
    // We create a constructor that takes in 2 parameters: context and the list of movies (later added orientation)
    public MovieArrayAdapter(Context context, List<Movie> movies, int orientation){
        super(context, android.R.layout.simple_expandable_list_item_1, movies);

        orient = orientation;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    // get the data item for position
        Movie movie = getItem(position);

        // check if the existing view is being reused
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false); // setting to false so we make the changes ourselves

        }

        // we now get references to the elements in the layout and populate accordingly
        // find the image view
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
        // clear out image from convertView
        ivImage.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);

        // Populating the views with the appropriate data

        tvTitle.setText(movie.getOrginalTitle());
        tvOverview.setText(movie.getOverview());

        // Here we have to check the orientation of the phone. Portrait --> poster and landscapte --> backdrop
        if (orient == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(getContext()).load(movie.getPosterPath()).placeholder(R.drawable.portraitholder).error(R.drawable.portraitholder).into(ivImage);
        }
        else if(orient == Configuration.ORIENTATION_LANDSCAPE){
            Picasso.with(getContext()).load(movie.getBackdropPath()).placeholder(R.drawable.landscapeholder).error(R.drawable.landscapeholder).into(ivImage);
        }


        // return the view that just got populated
        return convertView;

           }



}
