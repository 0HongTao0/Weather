package com.hongtao.weather.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author：hongtao on 2017/7/20/020 10:53
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class Weather {

    private List<HeWeatherBean> HeWeather;

    public List<HeWeatherBean> getHeWeather() {
        return HeWeather;
    }

    public void setHeWeather(List<HeWeatherBean> HeWeather) {
        this.HeWeather = HeWeather;
    }

    public static class HeWeatherBean {
        private AqiBean aqi;
        private BasicBean basic;
        private NowBean now;
        @SerializedName("daily_forecast")
        private List<DailyForecastBean> dailyForecast;
        @SerializedName("hourly_forecast")
        private List<HourlyForecastBean> hourlyForecast;

        public AqiBean getAqi() {
            return aqi;
        }

        public void setAqi(AqiBean aqi) {
            this.aqi = aqi;
        }

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public NowBean getNow() {
            return now;
        }

        public void setNow(NowBean now) {
            this.now = now;
        }

        public List<DailyForecastBean> getDailyForecast() {
            return dailyForecast;
        }

        public void setDailyForecast(List<DailyForecastBean> dailyForecast) {
            this.dailyForecast = dailyForecast;
        }

        public List<HourlyForecastBean> getHourlyForecast() {
            return hourlyForecast;
        }

        public void setHourlyForecast(List<HourlyForecastBean> hourlyForecast) {
            this.hourlyForecast = hourlyForecast;
        }

        public static class AqiBean {
            private CityBean city;

            public CityBean getCity() {
                return city;
            }

            public void setCity(CityBean city) {
                this.city = city;
            }

            public static class CityBean {

                private String qlty;

                public String getQlty() {
                    return qlty;
                }

                public void setQlty(String qlty) {
                    this.qlty = qlty;
                }
            }


        }

        public static class BasicBean {

            private String id;
            private String city;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }
        }

        public static class NowBean {
            private CondBean cond;
            @SerializedName("tmp")
            private String temperature;
            private WindBean wind;

            public CondBean getCond() {
                return cond;
            }

            public void setCond(CondBean cond) {
                this.cond = cond;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public WindBean getWind() {
                return wind;
            }

            public void setWind(WindBean wind) {
                this.wind = wind;
            }

            public static class CondBean {
                private String code;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }
            }

            public static class WindBean {

                @SerializedName("dir")
                private String windDirection;
                @SerializedName("spd")
                private String windSpeed;

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
            }
        }

        public static class DailyForecastBean {

            private CondBeanX cond;
            private String date;
            private TmpBean tmp;
            private WindBeanX wind;

            public CondBeanX getCond() {
                return cond;
            }

            public void setCond(CondBeanX cond) {
                this.cond = cond;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public TmpBean getTmp() {
                return tmp;
            }

            public void setTmp(TmpBean tmp) {
                this.tmp = tmp;
            }

            public WindBeanX getWind() {
                return wind;
            }

            public void setWind(WindBeanX wind) {
                this.wind = wind;
            }

            public static class CondBeanX {
                @SerializedName("code_d")
                private String codeDay;
                @SerializedName("code_n")
                private String codeNight;

                public String getCodeDay() {
                    return codeDay;
                }

                public void setCodeDay(String codeDay) {
                    this.codeDay = codeDay;
                }

                public String getCodeNight() {
                    return codeNight;
                }

                public void setCodeNight(String codeNight) {
                    this.codeNight = codeNight;
                }
            }

            public static class TmpBean {
                @SerializedName("max")
                private String maxTemperature;
                @SerializedName("min")
                private String minTemperature;

                public String getMaxTemperature() {
                    return maxTemperature;
                }

                public void setMaxTemperature(String maxTemperature) {
                    this.maxTemperature = maxTemperature;
                }

                public String getMinTemperature() {
                    return minTemperature;
                }

                public void setMinTemperature(String minTemperature) {
                    this.minTemperature = minTemperature;
                }
            }

            public static class WindBeanX {
                @SerializedName("dir")
                private String windDirection;
                @SerializedName("spd")
                private String windSpeed;

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
            }
        }

        public static class HourlyForecastBean {


            private CondBeanXX cond;
            private String date;
            @SerializedName("tmp")
            private String temperature;
            private WindBeanXX wind;

            public CondBeanXX getCond() {
                return cond;
            }

            public void setCond(CondBeanXX cond) {
                this.cond = cond;
            }

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

            public WindBeanXX getWind() {
                return wind;
            }

            public void setWind(WindBeanXX wind) {
                this.wind = wind;
            }

            public static class CondBeanXX {

                private String code;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }
            }

            public static class WindBeanXX {
                @SerializedName("dir")
                private String windDirection;
                @SerializedName("spd")
                private String windSpeed;

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
            }
        }
    }
}
