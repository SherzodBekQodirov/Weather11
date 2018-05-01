package ru.startandroid.weather.response;

public class Main {
    public double temp;

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
