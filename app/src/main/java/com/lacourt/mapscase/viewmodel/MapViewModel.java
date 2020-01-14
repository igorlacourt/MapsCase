package com.lacourt.mapscase.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lacourt.mapscase.repository.MapRepository;

public class MapViewModel extends AndroidViewModel {
    MapRepository repository = new MapRepository();
    public MutableLiveData cities = repository.cities;

    public MapViewModel(@NonNull Application application) {
        super(application);
    }

    public void getCitiesList(double latitude, double longitude) {
        repository.getCitiesList(latitude, longitude);
    }
}
