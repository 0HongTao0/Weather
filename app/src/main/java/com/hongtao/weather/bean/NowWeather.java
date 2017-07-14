package com.hongtao.weather.bean;

/**
 * author：Administrator on 2017/7/14/014 08:42
 * email：935245421@qq.com
 */
public class NowWeather {
    private String temperature;
    private String sky;
    private String windDirection;
    private String air;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }
}
