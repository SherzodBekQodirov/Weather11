package ru.startandroid.weather.response;

import java.io.Serializable;
import java.util.List;

public class MainParent implements Serializable {
    private Long dt;
    private Main main;
    private List<Weather> weather;

    public List<Weather> getWeatherList() {
        return weather;
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
                ", weatherList=" + weather +
                '}';
    }
}