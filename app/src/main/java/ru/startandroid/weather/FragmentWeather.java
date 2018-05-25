package ru.startandroid.weather;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
//import android.support.design.widget.FloatingActionButton;

/**
 * Created by sher on 5/13/18.
 */

public class FragmentWeather extends Fragment {

    private FloatingActionButton btn1;
    private TextView textView, textView2, textView3;
    private ImageView imgview;
    private Bitmap bitmap;
    final String LOG_TAG = "myLogs";
    long id;
    private static final String DEFAULT_VERSION = "2.5";

    int pageNumber;
    static FragmentWeather newInstance(String city) {
        FragmentWeather pageFragment = new FragmentWeather();
        Bundle bundle = new Bundle();
        bundle.putString("city", city);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }



    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String nameofcites;
        Bundle extras = getArguments();
        nameofcites = extras.getString("city");
        View v = inflater.inflate(R.layout.fragment, null);
        btn1 = (FloatingActionButton) v.findViewById(R.id.floatingActionButton2);
        textView = (TextView) v.findViewById(R.id.textView);
        textView2 = (TextView) v.findViewById(R.id.textView2);
        textView3 = (TextView) v.findViewById(R.id.textView3);
        imgview = (ImageView) v.findViewById(R.id.imageView);
        setHasOptionsMenu(true);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
        btn1.setOnClickListener(new View.OnClickListener() {
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



        Call<ResponseApi> call = link.getData(nameofcites, "a7dc109561ec63ddd24cd4df691e3043");
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
                        textView.setText(String.valueOf(temps) + "°");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(MenuItem item){
        MenuItem menuItem;
        switch (item.getItemId()){
            case R.id.changecity:
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(R.layout.activity_cange_cites, null);
            Log.d(LOG_TAG, "inflate");
                return true;
            case R.id.addcity:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    }

