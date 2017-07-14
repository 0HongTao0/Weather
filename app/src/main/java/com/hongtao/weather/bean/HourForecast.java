package com.hongtao.weather.bean;

/**
 * author：Administrator on 2017/7/13/013 10:10
 * email：935245421@qq.com
 */
public class HourForecast {
    private String time;
    private String sky;
    private String temperature;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
