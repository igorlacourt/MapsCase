package com.lacourt.mapscase.network;

import com.lacourt.mapscase.data.CitiesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("data/2.5/find")
    Call<CitiesList> getCitiesList(@Query("lat") double latitude, @Query("lon") double longitude, @Query("cnt") int count);
}
