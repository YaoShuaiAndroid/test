package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by bt on 2018/2/1.
 */

public class ProviceModel {
    private String id;
    private String name;
    private boolean is_select;
    List<CityModel> cityList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityModel> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityModel> cityList) {
        this.cityList = cityList;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }
}
