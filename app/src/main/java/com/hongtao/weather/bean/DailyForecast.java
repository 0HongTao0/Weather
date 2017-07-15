package com.hongtao.weather.bean;

/**
 * author：Administrator on 2017/7/14/014 09:58
 * email：935245421@qq.com
 */
public class DailyForecast {
    private String date;
    private String temperature;
    private String daySky;
    private String nightSky;
    private String windDirection;
    private String windSpeed;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDaySky() {
        return daySky;
    }

    public void setDaySky(String daySky) {
        this.daySky = daySky;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getNightSky() {
        return nightSky;
    }

    public void setNightSky(String nightSky) {
        this.nightSky = nightSky;
    }
}
