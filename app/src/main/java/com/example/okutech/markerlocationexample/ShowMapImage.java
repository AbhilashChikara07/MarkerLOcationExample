package com.example.okutech.markerlocationexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Description
 *
 * @author Abhilash Chikara
 * @version 1.0
 * @since 8/9/17
 */

public class ShowMapImage extends SuperActivity {
    private ImageView showMapImage;
    private double latitude = 28.4595;
    private double longitude = 77.0266;
    private String secondUrl = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=" +
            latitude +
            "," +
            longitude +
            "&zoom=13&size=600x300&maptype=roadmap\n" +
            "&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318\n" +
            "&markers=color:red%7Clabel:C%7C40.718217,-73.998284\n";

    private String urlPath = "http://maps.google.com/maps/api/staticmap?center=" + latitude + ","
            + longitude + "&zoom=15&size=1000x1000&sensor=false";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map_image);
        showMapImage = (ImageView) findViewById(R.id.showMapImage);
        Glide.with(this).load(urlPath).centerCrop().into(showMapImage);
    }
}
