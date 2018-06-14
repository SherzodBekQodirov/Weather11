package ru.startandroid.weather.data;

public interface ResponseListener {
    void success(ResponseApi api);
    void error(Exception e);
}
