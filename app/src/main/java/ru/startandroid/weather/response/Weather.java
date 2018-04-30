package ru.startandroid.weather.response;



public class Weather {
    public String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "icon='" + icon + '\'' +
                '}';
    }
}
