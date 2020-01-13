package com.lacourt.mapscase.data;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class Sys {
    @Expose
    private String country;
    @Expose
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}