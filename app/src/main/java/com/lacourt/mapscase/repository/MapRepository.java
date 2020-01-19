package com.lacourt.mapscase.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.lacourt.mapscase.data.CitiesList;
import com.lacourt.mapscase.domainobjects.CityDomainObject;
import com.lacourt.mapscase.domainobjects.MapFunction;
import com.lacourt.mapscase.network.ApiFactory;
import com.lacourt.mapscase.network.Resource;
import com.lacourt.mapscase.network.Error;

import java.util.List;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.HttpException;

public class MapRepository {
    public MutableLiveData<Resource<List<CityDomainObject>>> cities;

    public MapRepository() {
        this.cities = new MutableLiveData();
    }

    public void getCitiesList(double latitude, double longitude) {
        Log.d("requestlog", "repository, getCitiesList()");
        cities.setValue(new Resource().loading());
        ApiFactory.getWeatherApi().getCitiesList(latitude, longitude, 15).enqueue(new Callback<CitiesList>() {
            @Override
            public void onResponse(Call<CitiesList> call, Response<CitiesList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        cities.setValue(
                              new Resource<List<CityDomainObject>>().success(MapFunction.mapCitiesList(response.body().getCities()))
                        );
                    }
                } else {
                    cities.setValue(
                          new Resource<List<CityDomainObject>>().error(new Error(response.code(), response.message()))
                    );
                }
            }

            @Override
            public void onFailure(Call<CitiesList> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    cities.setValue(
                            new Resource<List<CityDomainObject>>().error(new Error(408, t.getMessage()))
                    );
                } else if (t instanceof UnknownHostException) {
                    cities.setValue(
                            new Resource<List<CityDomainObject>>().error(new Error(503, t.getMessage()))
                    );
                } else if (t instanceof HttpException) {
                    cities.setValue(
                            new Resource<List<CityDomainObject>>().error(new Error(400, t.getMessage()))
                    );
                } else {
                    cities.setValue(
                            new Resource<List<CityDomainObject>>().error(new Error(99, t.getMessage()))
                    );
                }
            }
        });
    }
}
