package ru.startandroid.weather.data;

import ru.startandroid.weather.data.response.ResponseApi;

public interface ResponseListener {
    void success(ResponseApi api);
    void error(Exception e);
}
