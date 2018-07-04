package ru.startandroid.weather.data.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MainParent implements Serializable {
    private Long dt;
    private Main main;
    private Wind wind;
    private List<Weather> weather;
    @SerializedName("dt_txt")
    private Date date;

    public List<Weather> getWeatherList() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
                ", dt_txt='" + date + '\'' +
                '}';
    }
}