package ru.startandroid.weather.data.net;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.startandroid.weather.data.response.ResponseApi;

public interface LinkService {
    @GET("forecast")
    Call<ResponseApi> getData(@Query("q") String name, @Query("APPID") String appId);
}

