package ru.startandroid.weather;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Link {
    @GET("forecast?id=1512440&APPID=a7dc109561ec63ddd24cd4df691e3043")
    Call<ResponseApi> getData(@Query("id") long id, @Query("APPID") String appId);


}

