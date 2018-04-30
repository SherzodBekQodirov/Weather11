package ru.startandroid.weather;
import java.util.List;

import ru.startandroid.weather.response.MainParent;
import ru.startandroid.weather.response.Weather;

public class ResponseApi {
    public List<MainParent> list;
    public List<Weather> weatherList;
    String code;
    Float message;
    int cnt;

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public List<MainParent> getList(String temp) {
        return list;
    }

    public void setList(List<MainParent> list) {
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getMessage() {
        return message;
    }

    public void setMessage(Float message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "ResponseApi{" +
                "list=" + list +
                ", code='" + code + '\'' +
                ", message=" + message +
                ", cnt=" + cnt +
                '}';
    }
}


