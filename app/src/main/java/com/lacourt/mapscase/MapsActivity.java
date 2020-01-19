package com.lacourt.mapscase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.InLocoEngagementOptions;
import com.inlocomedia.android.engagement.request.FirebasePushProvider;
import com.inlocomedia.android.engagement.request.PushProvider;
import com.lacourt.mapscase.data.City;
import com.lacourt.mapscase.firebasemessage.AppsMessageService;
import com.lacourt.mapscase.network.Resource;
import com.lacourt.mapscase.ui.BottomSheetCities;
import com.lacourt.mapscase.viewmodel.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private MarkerOptions marker;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Button btnSearch = findViewById(R.id.btn_search);
        btnSearchClick(btnSearch);
    }

    private void initInLoco() {
        // Set initialization options
        InLocoEngagementOptions options = InLocoEngagementOptions.getInstance(this);
        // The App ID you obtained in the dashboard
        options.setApplicationId(BuildConfig.IN_LOCO_APP_ID);
        // Remember to set to false in production builds.
        options.setLogEnabled(true);
        InLocoEngagement.init(this, options);
    }

    private void btnSearchClick(Button btnSearch) {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetCities.newInstance(latitude, longitude)
                        .show(getSupportFragmentManager(), BottomSheetCities.TAG);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        LatLng currentLocation = new LatLng(-23.6821604,-46.8754915);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(lm != null){
            @SuppressLint("MissingPermission")
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                currentLocation = new LatLng(latitude, longitude);
            }
        }

        marker = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Local Escolhido");

        mMap.addMarker(marker);
        if(currentLocation != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    private void onMapClick() {
        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                // Setting the position for the marker
                marker.position(latLng);
                // Setting the title for the marker.
                // This will be displayed on taping the marker
                marker.title(latLng.latitude + " : " + latLng.longitude);
                // Clears the previously touched position
                mMap.clear();
                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                // Placing a marker on the touched position
                mMap.addMarker(marker);

                //set the current latitude and longitude
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }
        });
    }

    private void sendFCMTokenToInLoco(){
        // Retrieve the Firebase token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(AppsMessageService.TAG, "MapsActivity, getInstanceId failed", task.getException());
                            return;
                        }
                        Log.d(AppsMessageService.TAG, "MapsActivity, getInstanceId successful");
                        String token = null;
                        // Get new Instance ID token
                        if(task.getResult() != null)
                            token = task.getResult().getToken();

                        if (token != null && !token.isEmpty()) {
                            Log.d(AppsMessageService.TAG,
                                    "MapsActivity, sending token to InLoco... Token = " +
                                    token
                            );
                            final PushProvider pushProvider = new FirebasePushProvider.Builder()
                                    .setFirebaseToken(token)
                                    .build();
                            InLocoEngagement.setPushProvider(MapsActivity.this, pushProvider);
                        }
                    }
                });
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initialSetUp();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE
            );
        }
    }

    private void initialSetUp() {
        setUpMap();
        onMapClick();
        initInLoco();
        sendFCMTokenToInLoco();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (
                    permissions.length == 2 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    permissions[1].equals(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initialSetUp();
            } else {
                noPermissionDialog();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        requestPermission();
    }

    private void noPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sem permissão")
                .setMessage("Você precisa conceder acesso à sua localização para que o app funcione.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}