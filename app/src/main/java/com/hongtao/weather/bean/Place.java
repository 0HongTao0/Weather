package com.hongtao.weather.bean;

/**
 * author：hongtao on 2017/7/18/018 13:29
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class Place {
    private String id;
    private String name;
    private String aboveId;
    private String weatherId;

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

    public String getAboveId() {
        return aboveId;
    }

    public void setAboveId(String aboveId) {
        this.aboveId = aboveId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
