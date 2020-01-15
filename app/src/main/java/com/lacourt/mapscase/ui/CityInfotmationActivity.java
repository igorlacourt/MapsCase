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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_information);

        Intent intent = getIntent();

        TextView cityName = findViewById(R.id.tv_city_name);
        TextView maxTemp = findViewById(R.id.tv_max_temp);
        TextView minTemp = findViewById(R.id.tv_min_temp);
        TextView weatherDesrcription = findViewById(R.id.tv_weather_description);

        cityName.setText(intent.getStringExtra(CITY_NAME));
        maxTemp.setText(intent.getStringExtra(MAX_TEMP));
        minTemp.setText(intent.getStringExtra(MIN_TEMP));
        weatherDesrcription.setText(intent.getStringExtra(WEATHER_DESCRIPTION));
    }

    public static void startCityInformationActivity(Activity activity){

    }
}
