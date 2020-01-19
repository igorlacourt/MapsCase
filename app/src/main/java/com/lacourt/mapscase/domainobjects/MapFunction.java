package com.lacourt.mapscase.domainobjects;

import com.lacourt.mapscase.data.City;
import com.lacourt.mapscase.data.Main;
import com.lacourt.mapscase.data.Weather;

import java.util.ArrayList;
import java.util.List;

public class MapFunction {
    public static List<CityDomainObject> mapCitiesLis(List<City> cities){
        List<CityDomainObject> domainCities = new ArrayList();
        for(int i = 0; i < cities.size(); i++){
            City city = cities.get(i);
            if(city != null) {
                domainCities.add(
                        new CityDomainObject(
                                city.getName(),
                                city.getMain().getFeelsLike(),
                                city.getMain().getTempMin(),
                                city.getMain().getTempMax(),
                                city.getWeather().get(0).getDescription()
                        )
                );
            }
        }

        return domainCities;
    }
}
