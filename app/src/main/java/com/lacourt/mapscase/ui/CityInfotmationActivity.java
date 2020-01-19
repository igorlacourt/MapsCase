package com.lacourt.mapscase.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lacourt.mapscase.R;

public class CityInfotmationActivity extends AppCompatActivity {
    public static final String CITY_NAME = "com.lacourt.mapscase.ui.CityInfotmationActivity.citytame";
    public static final String MAX_TEMP = "com.lacourt.mapscase.ui.CityInfotmationActivity.maxtemp";
    public static final String MIN_TEMP = "com.lacourt.mapscase.ui.CityInfotmationActivity.mintemp";
    public static final String WEATHER_DESCRIPTION = "com.lacourt.mapscase.ui.CityInfotmationActivity.cityname";
    private final int DEFAULT_VALUE_FOR_NULL_CASE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_information);

        Intent intent = getIntent();

        TextView cityName = findViewById(R.id.tv_city_name);
        TextView maxTemp = findViewById(R.id.tv_max_temp);
        TextView minTemp = findViewById(R.id.tv_min_temp);
        TextView weatherDesrcription = findViewById(R.id.tv_weather_description);

        String sCityName = intent.getStringExtra(CITY_NAME);
        int dMinTemp = intent.getIntExtra(MIN_TEMP, DEFAULT_VALUE_FOR_NULL_CASE);
        int dMaxTemp = intent.getIntExtra(MAX_TEMP, DEFAULT_VALUE_FOR_NULL_CASE);
        String sWeatherDescription = intent.getStringExtra(WEATHER_DESCRIPTION);

        if(dMinTemp == DEFAULT_VALUE_FOR_NULL_CASE){
            minTemp.setText(" - ");
        } else {
            minTemp.setText(String.valueOf(dMinTemp));
        }
        if(dMaxTemp == DEFAULT_VALUE_FOR_NULL_CASE){
            maxTemp.setText(" - ");
        } else {
            maxTemp.setText(String.valueOf(dMaxTemp));
        }
        cityName.setText(sCityName);

        weatherDesrcription.setText(sWeatherDescription);
    }

    public static void startCityInformationActivity(Activity activity){

    }
}
