package ru.startandroid.weather.response;

public class Main {
    public int temp;

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Main{" +
                "temp=" + temp +
                '}';
    }
}
