package ru.startandroid.weather;
import java.util.List;

import ru.startandroid.weather.response.City;
import ru.startandroid.weather.response.MainParent;

public class ResponseApi {
    public List<MainParent> list;
    public City city;
    String code;
    Float message;
    int cnt;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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
                ", city=" + city +
                ", code='" + code + '\'' +
                ", message=" + message +
                ", cnt=" + cnt +
                '}';
    }
}