package ru.startandroid.weather.response;

import java.io.Serializable;
import java.util.List;

public class MainParent implements Serializable {
    private Long dt;
    private Main main;
    private Wind wind;
    private List<Weather> weather;
    private String dt_txt;

    public List<Weather> getWeatherList() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weather = weatherList;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
    @Override
    public String toString() {
        return "MainParent{" +
                "dt=" + dt +
                ", main=" + main +
                ", wind=" + wind +
                ", weather=" + weather +
                ", dt_txt='" + dt_txt + '\'' +
                '}';
    }
}