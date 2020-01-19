package com.lacourt.mapscase.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lacourt.mapscase.R;
import com.lacourt.mapscase.domainobjects.CityDomainObject;
import com.lacourt.mapscase.network.Resource;
import com.lacourt.mapscase.ui.adapter.CitiesAdapter;
import com.lacourt.mapscase.ui.adapter.CityClick;
import com.lacourt.mapscase.viewmodel.MapViewModel;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import com.lacourt.mapscase.network.Error;

public class BottomSheetCities extends BottomSheetDialogFragment implements CityClick {
    private static final String BUNDLE_LATITUTDE = "com.lacourt.mapscase.ui.latitude";
    private static final String BUNDLE_LONGITUDE = "com.lacourt.mapscase.ui.longitude";
    public static final String TAG = "com.lacourt.mapscase.ui.BottomSheetCities.TAG";
    private MapViewModel viewModel;
    private RecyclerView recyclerView;
    private CitiesAdapter adapter;
    private ProgressBar loadingCities;
    private TextView errorMessage;

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_cities_layout, null);
        loadingCities = contentView.findViewById(R.id.pb_loading_cities);
        errorMessage = contentView.findViewById(R.id.tv_error_message);

        initCitiesList(contentView);

        double latitude = getArguments().getDouble(BUNDLE_LATITUTDE);
        double longitude = getArguments().getDouble(BUNDLE_LONGITUDE);

        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        viewModel.getCitiesList(latitude, longitude);
        viewModel.cities.observe(this, new Observer<Resource<List<CityDomainObject>>>() {
            @Override
            public void onChanged(Resource<List<CityDomainObject>> cities) {
                Log.d("requestlog", "BottomSheet, onChanged()");
               handleResponse(cities);
            }
        });
        dialog.setContentView(contentView);
    }

    private void handleResponse(Resource<List<CityDomainObject>> cities) {
        switch(cities.status) {
            case SUCCESS:
                Log.d("requestlog", "BottomSheet, case SUCCESS");
                passDataToRecyclerView(cities.data);
                loadingCities.setVisibility(View.INVISIBLE);
                errorMessage.setVisibility(View.INVISIBLE);
                break;
            case LOADING:
                Log.d("requestlog", "BottomSheet, case LOADING");
                loadingCities.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.INVISIBLE);
                break;
            case ERROR:
                Log.d("requestlog", "BottomSheet, case ERROR: " + cities.error.getMessage());
                loadingCities.setVisibility(View.INVISIBLE);
                errorMessage.setText(cities.error.getMessage());
                errorMessage.setVisibility(View.VISIBLE);
                break;
            default:
                loadingCities.setVisibility(View.INVISIBLE);
                errorMessage.setText(Error.GENERIC);
                errorMessage.setVisibility(View.VISIBLE);
        }
    }

    private void passDataToRecyclerView(List<CityDomainObject> cities) {
        adapter.setCities(cities);
    }

    private void initCitiesList(View contentView) {
        CityClick cityClick = this;
        List<CityDomainObject> citiesList = new ArrayList<>();
        recyclerView = contentView.findViewById(R.id.rv_cities);
        adapter = new CitiesAdapter(getContext(), cityClick, citiesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCityClick(String city, Integer minTemp, Integer maxTemp, String weatherDescription) {
        Intent intent = new Intent(getContext(), CityInfotmationActivity.class);
        intent.putExtra(CityInfotmationActivity.CITY_NAME, city);
        intent.putExtra(CityInfotmationActivity.MIN_TEMP, minTemp);
        intent.putExtra(CityInfotmationActivity.MAX_TEMP, maxTemp);
        intent.putExtra(CityInfotmationActivity.WEATHER_DESCRIPTION, weatherDescription);
        startActivity(intent);
    }

    public static BottomSheetCities newInstance(double lat, double lon) {
        BottomSheetCities bottomSheetFragment = new BottomSheetCities();
        Bundle bundle = new Bundle();
        bundle.putDouble(BUNDLE_LATITUTDE, lat);
        bundle.putDouble(BUNDLE_LONGITUDE, lon);
        bottomSheetFragment.setArguments(bundle);

        return bottomSheetFragment ;
    }
}
