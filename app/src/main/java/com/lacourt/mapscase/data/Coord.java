package com.lacourt.mapscase.data;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class Coord {
    @Expose
    private Double lat;
    @Expose
    private Double lon;
    @Expose
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}