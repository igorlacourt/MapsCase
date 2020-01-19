package com.lacourt.mapscase.domainobjects;

public class CityDomainObject {
    private String name;
    private Integer feelsLike;
    private Integer tempMin;
    private Integer tempMax;
    private String description;

    public CityDomainObject(String name, Double feelsLike, Double tempMin, Double tempMax, String description) {
        this.name = name;
        this.feelsLike = feelsLike.intValue();
        this.tempMin = tempMin.intValue();
        this.tempMax = tempMax.intValue();
        this.description = description;
        convertTempToCelsius();
        nullCheckFields();
    }

    private void convertTempToCelsius() {
        if(this.feelsLike != null){
            this.feelsLike -= 274;
        }
        if(this.tempMin != null){
            this.tempMin -= 274;
        }
        if(this.tempMax != null){
            this.tempMax -= 274;
        }
    }

    private void nullCheckFields() {
        if(this.name == null || this.name.equals("")){
            this.name = "Nome da cidade não disponível";
        }
        if(this.description == null || this.description.equals("")){
            this.name = "Descrição não disponível";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Integer feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Integer getTempMin() {
        return tempMin;
    }

    public void setTempMin(Integer tempMin) {
        this.tempMin = tempMin;
    }

    public Integer getTempMax() {
        return tempMax;
    }

    public void setTempMax(Integer tempMax) {
        this.tempMax = tempMax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
