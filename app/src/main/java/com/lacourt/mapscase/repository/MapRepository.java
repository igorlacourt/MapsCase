package com.lacourt.mapscase.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.lacourt.mapscase.data.CitiesList;
import com.lacourt.mapscase.data.City;
import com.lacourt.mapscase.domainobjects.CityDomainObject;
import com.lacourt.mapscase.domainobjects.MapFunction;
import com.lacourt.mapscase.network.ApiFactory;
import com.lacourt.mapscase.network.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapRepository {
    public MutableLiveData<Resource<List<CityDomainObject>>> cities;

    public MapRepository(){
        this.cities = new MutableLiveData();
    }

    public void getCitiesList(double latitude, double longitude){
        Log.d("requestlog", "repository, getCitiesList()");
        cities.setValue(
                new Resource<List<CityDomainObject>>(Resource.Status.LOADING, null, null)
        );
        ApiFactory.getWeatherApi().getCitiesList(latitude, longitude, 15).enqueue(new Callback<CitiesList>() {
            @Override
            public void onResponse(Call<CitiesList> call, Response<CitiesList> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){

                        cities.setValue(
                                new Resource<>(Resource.Status.SUCCESS, MapFunction.mapCitiesLis(response.body().getCities()), null)
                        );
                    }
                } else {
                    new Resource<>(Resource.Status.ERROR, null, new Error());
//                    Toast.makeText(ctx, "Http Error!", Toast.LENGTH_LONG).show();
                    Log.d("requestlog", "repository, isSuccessful = " + response.isSuccessful());
                    Log.d("requestlog", "repository, code = " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CitiesList> call, Throwable t) {
                Log.d("requestlog", "repository, onFailure = " + t.getMessage());
            }
        });
    }
}
