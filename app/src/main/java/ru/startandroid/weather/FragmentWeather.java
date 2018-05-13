package ru.startandroid.weather;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.weather.response.MainParent;
import ru.startandroid.weather.response.Weather;

/**
 * Created by sher on 5/13/18.
 */

public class FragmentWeather extends Fragment {
    private Button btn;
    private TextView textView, textView2, textView3;
    private ImageView imgview;
    private Bitmap bitmap;
    final String LOG_TAG = "myLogs";
    long id;
    private static final String DEFAULT_VERSION = "2.5";

    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment, null);
        btn = (Button) v.findViewById(R.id.button);
        textView = (TextView) v.findViewById(R.id.textView);
        textView2 = (TextView) v.findViewById(R.id.textView2);
        textView3 = (TextView) v.findViewById(R.id.textView3);
        imgview = (ImageView) v.findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                imgview.startAnimation(animation);
            }
        });

        final String url =
                "http://api.openweathermap.org/data/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(url+DEFAULT_VERSION+"/")
                .build();
        final Link link = retrofit.create(Link.class);


        Call<ResponseApi> call = link.getData(id, "APPID");
        call.enqueue(new Callback<ResponseApi>() {

            @Override
            public void onResponse(final Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.isSuccessful()){
                    Log.d(LOG_TAG, "response"+response.body().toString());
                } else {
                    try {
                        Log.d(LOG_TAG, "response full errors"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                final ResponseApi myResponse = response.body();
                final MainParent temp = myResponse.list.get(0);
                List<Weather> weatherList = temp.getWeatherList();
                int tempk = (int) temp.getMain().getTemp();
                final int temps = (tempk - 273);
                final String main = weatherList.get(0).getMain();
                String icon = weatherList.get(0).getIcon();
                final String iconUrls = "http://openweathermap.org/img/w/" + icon + ".png";



                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(String.valueOf(temps) + "°C");
                        textView2.setText(main);
                        textView3.setText(myResponse.city.getName());
                        Picasso.get().load(iconUrls).into(imgview);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                //agarda zapros serverga yetib bormasa
            }
        });

    return v;
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
//                Toast.makeText(this, "Item 1 bosildi", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    }

