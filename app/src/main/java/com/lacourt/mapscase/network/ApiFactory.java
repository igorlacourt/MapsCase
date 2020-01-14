package com.lacourt.mapscase.network;

import com.lacourt.mapscase.AppConstants;
import com.lacourt.mapscase.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    //Creating Auth Interceptor to add api_key query in front of all the requests.
    private static Interceptor getAuthInterceptor() {
       Interceptor authInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                HttpUrl url = chain.request().url()
                        .newBuilder()
                        .addQueryParameter("APPID", BuildConfig.WEATHER_API_KEY)
                        .build();

                Request newRequest = chain.request()
                        .newBuilder()
                        .url(url)
                        .build();

                return chain.proceed(newRequest);
            }
        };
       return authInterceptor;
    }

    private static OkHttpClient loggingClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(getAuthInterceptor())
                .build();
    }

     private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.WEATHER_BASE_URL)
            .client(loggingClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    private static WeatherApi weatherApi = null;

    public static WeatherApi getWeatherApi(){
        if(weatherApi == null){
            weatherApi = retrofit.create(WeatherApi.class);
        }
        return weatherApi;
    }
}
