package ru.startandroid.weather.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.weather.Link;
import ru.startandroid.weather.ResponseApi;

import static ru.startandroid.weather.Constants.API_KEY;
import static ru.startandroid.weather.Constants.BASE_URL;
import static ru.startandroid.weather.Constants.DEFAULT_VERSION;

public class CityFetcher {

    private final Link link;
    private Call<ResponseApi> call;
    private ResponseListener mListener;

    public CityFetcher(){
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL + DEFAULT_VERSION + "/")
                .build();
        link = retrofit.create(Link.class);
    }

    public void fetchCity(String cityName){
        call = link.getData(cityName, API_KEY);
        call.enqueue(callback);
    }

    public void onDestroy(){
        if(call != null && !call.isCanceled()){
            call.cancel();
        }
    }

    private Callback<ResponseApi> callback = new Callback<ResponseApi>() {
        @Override
        public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
            if(!response.isSuccessful()){
                showError(new Exception(response.errorBody().toString()));
                return;
            }

            if(response.body() == null){
                showError(new Exception("город не найден"));
                return;
            }

            if(mListener != null){
                mListener.success(response.body());
            }
        }

        @Override
        public void onFailure(Call<ResponseApi> call, Throwable t) {

        }
    };


    private void showError(Exception e){
        if(mListener != null){
            mListener.error(e);
        }
    }

    public void setListener(ResponseListener listener){
        mListener = listener;
    }




}
