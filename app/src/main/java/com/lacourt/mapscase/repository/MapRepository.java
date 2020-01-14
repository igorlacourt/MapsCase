package com.lacourt.mapscase.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.lacourt.mapscase.MapsActivity;
import com.lacourt.mapscase.data.CitiesList;
import com.lacourt.mapscase.data.City;
import com.lacourt.mapscase.network.ApiFactory;
import com.lacourt.mapscase.network.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapRepository {
    public MutableLiveData<Resource<List<City>>> cities;

    public MapRepository(){
        this.cities = new MutableLiveData();
    }

    public void getCitiesList(double latitude, double longitude){
        new ApiFactory().weatherApi.getCitiesList(latitude, longitude, 15).enqueue(new Callback<CitiesList>() {
            @Override
            public void onResponse(Call<CitiesList> call, Response<CitiesList> response) {
                if(response.isSuccessful()){
//                    Toast.makeText(ctx, "Successful!", Toast.LENGTH_LONG).show();
                    if(response.body() != null){
                        cities.setValue((Resource<List<City>>) response.body().getCities());
                    }
                    Log.d("logrequest", "" + response.body().getCities().get(0).getName());
                    Log.d("logrequest", "" + response.body().getCities().get(0).getMain().getTempMax());
                    Log.d("logrequest", "" + response.body().getCities().get(0).getMain().getTempMin());
                    Log.d("logrequest", "" + response.body().getCities().get(0).getWeather().get(0).getDescription());
                } else {
//                    Toast.makeText(ctx, "Http Error!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CitiesList> call, Throwable t) {
//                Toast.makeText(ctx, "Failure!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
