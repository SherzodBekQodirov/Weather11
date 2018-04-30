package ru.startandroid.weather.response;

public class MainParent {
    public Long dt;
    public Main main;

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
                '}';
    }
}
