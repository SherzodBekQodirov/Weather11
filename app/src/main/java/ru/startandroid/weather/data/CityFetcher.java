package ru.startandroid.weather.data;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.weather.data.net.LinkService;
import ru.startandroid.weather.data.response.ResponseApi;

import static ru.startandroid.weather.data.net.Constants.API_KEY;
import static ru.startandroid.weather.data.net.Constants.BASE_URL;
import static ru.startandroid.weather.data.net.Constants.DEFAULT_VERSION;

public class CityFetcher {

    private final LinkService linkService;

    private final static CityFetcher INSTANCE = new CityFetcher();

    private CityFetcher() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Log.d("Gson", String.valueOf(gson));
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL + DEFAULT_VERSION + "/")
                .build();
        linkService = retrofit.create(LinkService.class);
    }

    public static CityFetcher getInstance() {
        return INSTANCE;
    }

    public void fetchCity(@NonNull final String cityName, @NonNull final ResponseListener mListener) {
        Call<ResponseApi> call = linkService.getData(cityName, API_KEY);
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
