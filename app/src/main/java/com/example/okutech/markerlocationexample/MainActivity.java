package com.example.okutech.markerlocationexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.okutech.markerlocationexample.R.id.map;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    Button button;
    Button takeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        takeImage = (Button) findViewById(R.id.takeImage);
        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowMapImage.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CaptureLocation.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        /*--getMapAsync(this) is must required to get callbacks--*/
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng sydney = new LatLng(28.4595, 77.0266);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.addMarker
                (new MarkerOptions()
                        .anchor(0.5f, 0.5f)
                        .title("abhilash")
                        .alpha(0.7f)
                        .flat(true)
                        .position(sydney))
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.4595, 77.0266), 12));

        /*--we can set map type. Like 5 types of maps are available--*/
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
    }
}
