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
import com.lacourt.mapscase.data.City;
import com.lacourt.mapscase.network.Resource;
import com.lacourt.mapscase.ui.BottomSheetCities;
import com.lacourt.mapscase.viewmodel.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 1;
    private MapViewModel viewModel;
    private GoogleMap mMap;
    private LatLng latLng;
    private Marker marker;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initCitiesList();
        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        viewModel.cities.observe(this, new Observer<Resource<List<City>>>() {
            @Override
            public void onChanged(Resource<List<City>> cities) {
                Log.d("requestlog", "MapsActivity, onChanged()");
                switch(cities.status) {
                    case SUCCESS:
                        BottomSheetCities bottomSheet = new BottomSheetCities();
                        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                        break;
                    case LOADING:
                        // code block
                        break;
                    case ERROR:
                        // code block
                        break;
                    default:
                        // code block
                }

            }
        });
        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearchClick(btnSearch);
    }

    private void initCitiesList() {
        List<City> citiesList = new ArrayList<City>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_cities);
        CitiesAdapter adapter = new CitiesAdapter(this, chatList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void btnSearchClick(Button btnSearch) {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getCitiesList(-20.3119172, -40.2862623);
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
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                currentLocation = new LatLng(longitude, latitude);
            }
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(currentLocation)
                .title("Local Escolhido");

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    private void onMapClick() {
        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setUpMap();
            onMapClick();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length == 2 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    permissions[1].equals(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpMap();
                onMapClick();
            } else {
                noPermissionDialog();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        requestPermission();
    }



    private void noPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sem permissão")
                .setMessage("Você precisa conceder acesso à sua localização para que o app funcione.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    private void setUpMapIfNeeded() {
//        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.map);
//
//            if (mapFragment != null)
//                mapFragment.getMapAsync(this);
//
//
//        }
//    }
}

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng point) {
//                //save current location
//                latLng = point;
//
//                City<Address> addresses = new ArrayList<>();
//                try {
//                    addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                android.location.Address address = addresses.get(0);
//
//                if (address != null) {
//                    StringBuilder sb = new StringBuilder();
//                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
//                        sb.append(address.getAddressLine(i) + "\n");
//                    }
//                    Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
//                }
//
//                //remove previously placed Marker
//                if (marker != null) {
//                    marker.remove();
//                }
//
//                //place marker where user just clicked
//                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
//
//            }
//        });