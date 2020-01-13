package com.lacourt.mapscase.data;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class Weather {

@Expose
private Integer id;
@Expose
private String main;
@Expose
private String description;
@Expose
private String icon;
@Expose
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getMain() {
return main;
}

public void setMain(String main) {
this.main = main;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getIcon() {
return icon;
}

public void setIcon(String icon) {
this.icon = icon;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}