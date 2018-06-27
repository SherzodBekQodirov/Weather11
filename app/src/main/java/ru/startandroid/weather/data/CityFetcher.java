package ru.startandroid.weather.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ru.startandroid.weather.data.Constants.API_KEY;
import static ru.startandroid.weather.data.Constants.BASE_URL;
import static ru.startandroid.weather.data.Constants.DEFAULT_VERSION;

public class CityFetcher {

    private final Link link;

    private final static CityFetcher INSTANCE = new CityFetcher();

    private CityFetcher() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL + DEFAULT_VERSION + "/")
                .build();
        link = retrofit.create(Link.class);
    }

    public static CityFetcher getInstance() {
        return INSTANCE;
    }

    public void setDate() {
// TODO shu yerda date ni bironta kurinishda olingda keyin saqlab quying Object field qilib
    }

    public void fetchCity(@NonNull final String cityName, @NonNull final ResponseListener mListener) {
        Call<ResponseApi> call = link.getData(cityName, API_KEY);
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (!response.isSuccessful()) {
                    String message = "город не найден";
                    if (response.errorBody() != null) {
                        message = response.errorBody().toString();
                    }
                    mListener.error(new Exception(message));
                } else {
                    mListener.success(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                mListener.error(new Exception(t));
            }
        });
    }
}
