package ru.startandroid.weather.data.response;

import java.io.Serializable;
import java.util.List;

import ru.startandroid.weather.data.response.City;
import ru.startandroid.weather.data.response.MainParent;

public class ResponseApi implements Serializable {
    private List<MainParent> list;
    private City city;
    private String code;
    private Float message;
    private int cnt;

    public List<MainParent> getList() {
        return list;
    }

    public City getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public Float getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    @Override
    public String toString() {
        return "ResponseApi{" +
                "list=" + list +
                ", city=" + city +
                ", code='" + code + '\'' +
                ", message=" + message +
                ", cnt=" + cnt +
                '}';
    }
}