package ru.startandroid.weather.response;

/**
 * Created by sher on 4/30/18.
 */

public class City {
    Long dt;
    String name;

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                "dt=" + dt +
                ", name='" + name + '\'' +
                '}';
    }
}
