package com.lacourt.mapscase.ui;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lacourt.mapscase.R;
import com.lacourt.mapscase.data.City;
import com.lacourt.mapscase.domainobjects.CityDomainObject;
import com.lacourt.mapscase.network.Resource;
import com.lacourt.mapscase.ui.adapter.CitiesAdapter;
import com.lacourt.mapscase.ui.adapter.CityClick;
import com.lacourt.mapscase.viewmodel.MapViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lacourt.mapscase.network.Resource.Status.ERROR;
import static com.lacourt.mapscase.network.Resource.Status.LOADING;
import static com.lacourt.mapscase.network.Resource.Status.SUCCESS;

public class BottomSheetCities extends BottomSheetDialogFragment implements CityClick {
    private MapViewModel viewModel;
    private RecyclerView recyclerView;
    private CitiesAdapter adapter;

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_cities_layout, null);

        initCitiesList(contentView);

        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        viewModel.getCitiesList(-20.3119172, -40.2862623);
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
                break;
            case LOADING:
                Log.d("requestlog", "BottomSheet, case SUCCESS");
                break;
            case ERROR:
                // code block
                break;
            default:
                // code block
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
}
