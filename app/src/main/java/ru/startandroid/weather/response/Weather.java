package ru.startandroid.weather.response;



public class Weather {
    public String icon;
    public String main;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "icon='" + icon + '\'' +
                ", main='" + main + '\'' +
                '}';
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
