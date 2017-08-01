package com.example.okutech.markerlocationexample;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Description
 *
 * @author Abhilash Chikara
 * @version 1.0
 * @since 8/1/17
 */

public class CaptureLocation extends SuperActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    double lat, lon;
    int acc;
//    private View view;
    private SupportMapFragment fragment;
    private LinearLayout captureLL;
    private TextView accuracy;
    private LocationManager mLocationManager;
    private boolean gps_enabled = false;
    private GoogleMap googleMap;
    private ProgressDialog progress;
    private int flag = 0;
    private AlertDialog dialog;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_demo);
        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        captureLL = (LinearLayout) findViewById(R.id.captureLL);
//        ImageView locationDrawable = (ImageView) captureLL.findViewById(R.id.locationDrawable);


        captureLL.setOnClickListener(captureLLClicked);
        accuracy = (TextView) findViewById(R.id.accuracy);

        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        progress = new ProgressDialog(this);
        progress.setMessage("Fetching your location...");

        showAlertDialog();
    }


    View.OnClickListener captureLLClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (acc > 40) {
                Toast.makeText(CaptureLocation.this, "Accuracy level is greater than 40m, Please Try Again",
                        Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2 * 1000); // Update location every second

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (checkGps()) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (progress != null && progress.isShowing()) {
                                progress.dismiss();
                            }
                            System.out.println("location1:  " + location);

                            lat = location.getLatitude();
                            lon = location.getLongitude();

                            acc = (int) location.getAccuracy();

                            if (flag == 0) {
                                if (googleMap != null) {
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(lat, lon), 15));
                                }
                                flag = 1;
                            }
                            captureLL.setVisibility(View.VISIBLE);

                            accuracy.setText("Accurate to " + acc + " meters");

                        } else {
                            showAlertDialog();
                        }
                    }
                });
    }

    private void showAlertDialog() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("GPS is disabled")
                .setMessage("Show location settings?")
                .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        dialog.dismiss();
                    }
                })
                .create();
    }

    private boolean checkGps() {
        gps_enabled = false;
        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gps_enabled;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        googleMap = map;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkGps()) {
            fragment.getMapAsync(this);
            progress.show();
            flag = 0;
        } else {
            dialog.show();
        }
        captureLL.setVisibility(View.GONE);
    }


    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }
}
