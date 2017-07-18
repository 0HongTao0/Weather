package com.hongtao.weather.bean;

import com.google.gson.annotations.SerializedName;

/**
 * author：Administrator on 2017/7/12/012 14:41
 * email：935245421@qq.com
 */
public class District extends City {
    @SerializedName("weather_id")
    private String weatherId;
    private String id;
    private String name;

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

    public String getWeatherId() {
        return weatherId;
    }


    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
