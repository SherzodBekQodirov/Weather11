package ru.startandroid.weather.response;

import java.io.Serializable;

/**
 * Created by sher on 4/30/18.
 */

public class City implements Serializable {
    private Long id;
    private String name;
    private Coord coord;
    private Long population;

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Long getDt() {
        return id;
    }

    public void setDt(Long dt) {
        this.id = dt;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", coord=" + coord +
                ", population=" + population +
                '}';
    }
}
