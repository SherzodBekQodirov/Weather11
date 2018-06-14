package ru.startandroid.weather.response;

import java.io.Serializable;

public class Main implements Serializable {
    private double temp;

    public double getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Main{" +
                "temp=" + temp +
                '}';
    }
}
