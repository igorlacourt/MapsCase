package com.lacourt.mapscase.data;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class Main {

@Expose
private Double temp;
@Expose
private Double feelsLike;
@Expose
private Double tempMin;
@Expose
private Double tempMax;
@Expose
private Integer pressure;
@Expose
private Integer humidity;
@Expose
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public Double getTemp() {
return temp;
}

public void setTemp(Double temp) {
this.temp = temp;
}

public Double getFeelsLike() {
return feelsLike;
}

public void setFeelsLike(Double feelsLike) {
this.feelsLike = feelsLike;
}

public Double getTempMin() {
return tempMin;
}

public void setTempMin(Double tempMin) {
this.tempMin = tempMin;
}

public Double getTempMax() {
return tempMax;
}

public void setTempMax(Double tempMax) {
this.tempMax = tempMax;
}

public Integer getPressure() {
return pressure;
}

public void setPressure(Integer pressure) {
this.pressure = pressure;
}

public Integer getHumidity() {
return humidity;
}

public void setHumidity(Integer humidity) {
this.humidity = humidity;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}