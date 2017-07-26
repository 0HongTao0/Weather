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
        private SuggestionBean suggestion;

        public SuggestionBean getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(SuggestionBean suggestion) {
            this.suggestion = suggestion;
        }

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

        public static class SuggestionBean {
            private AirBean air;
            private ComfBean comf;
            private CwBean cw;
            private DrsgBean drsg;
            private FluBean flu;
            private SportBean sport;
            private TravBean trav;
            private UvBean uv;

            public AirBean getAir() {
                return air;
            }

            public void setAir(AirBean air) {
                this.air = air;
            }

            public ComfBean getComf() {
                return comf;
            }

            public void setComf(ComfBean comf) {
                this.comf = comf;
            }

            public CwBean getCw() {
                return cw;
            }

            public void setCw(CwBean cw) {
                this.cw = cw;
            }

            public DrsgBean getDrsg() {
                return drsg;
            }

            public void setDrsg(DrsgBean drsg) {
                this.drsg = drsg;
            }

            public FluBean getFlu() {
                return flu;
            }

            public void setFlu(FluBean flu) {
                this.flu = flu;
            }

            public SportBean getSport() {
                return sport;
            }

            public void setSport(SportBean sport) {
                this.sport = sport;
            }

            public TravBean getTrav() {
                return trav;
            }

            public void setTrav(TravBean trav) {
                this.trav = trav;
            }

            public UvBean getUv() {
                return uv;
            }

            public void setUv(UvBean uv) {
                this.uv = uv;
            }

            public static class AirBean {
                /**
                 * brf : 中
                 * txt : 气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。
                 */

                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class ComfBean {
                /**
                 * brf : 较不舒适
                 * txt : 白天天气多云，同时会感到有些热，不很舒适。
                 */

                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class CwBean {
                /**
                 * brf : 较适宜
                 * txt : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
                 */

                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class DrsgBean {
                /**
                 * brf : 炎热
                 * txt : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
                 */

                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class FluBean {
                /**
                 * brf : 少发
                 * txt : 各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。
                 */

                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class SportBean {
                /**
                 * brf : 较不宜
                 * txt : 天气较好，无雨水困扰，但考虑气温很高，请注意适当减少运动时间并降低运动强度，运动后及时补充水分。
                 */

                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class TravBean {
                /**
                 * brf : 一般
                 * txt : 天气较好，同时有微风相伴，但温度较高，天气热，请尽量避免高温时段外出，若外出请注意防暑降温和防晒。
                 */

                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class UvBean {
                /**
                 * brf : 中等
                 * txt : 属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。
                 */

                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }
        }
    }

}
