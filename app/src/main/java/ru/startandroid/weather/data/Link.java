package ru.startandroid.weather.data;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Link {
    @GET("forecast")
    Call<ResponseApi> getData(@Query("q") String name, @Query("APPID") String appId);


}

