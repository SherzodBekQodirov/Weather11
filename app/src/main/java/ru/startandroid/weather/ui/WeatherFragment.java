package ru.startandroid.weather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.List;
import ru.startandroid.weather.R;
import ru.startandroid.weather.data.CityFetcher;
import ru.startandroid.weather.data.ResponseApi;
import ru.startandroid.weather.data.ResponseListener;
import ru.startandroid.weather.response.MainParent;
import ru.startandroid.weather.response.Weather;

/**
 * Created by sher on 5/13/18.
 */
public class WeatherFragment extends Fragment {
    private static final String EXTRA_CITY_NAME = "city_name";
    private CityFetcher mCityFetcher =  CityFetcher.getInstance();
    private String cityName;
    private FloatingActionButton btn1;
    private TextView textView, textView2, textView3;
    private ImageView imgview;


    static WeatherFragment newInstance(String cityName) {
        WeatherFragment pageFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CITY_NAME, cityName);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            cityName = getArguments().getString(EXTRA_CITY_NAME);
        }
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        btn1 = (FloatingActionButton) v.findViewById(R.id.floatingActionButton2);
        textView = (TextView) v.findViewById(R.id.textView);
        textView2 = (TextView) v.findViewById(R.id.textView2);
        textView3 = (TextView) v.findViewById(R.id.textView3);
        imgview = (ImageView) v.findViewById(R.id.imageView);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                imgview.startAnimation(animation);
                fetchData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    private void fetchData(){
        mCityFetcher.fetchCity(cityName, new ResponseListener() {
            @Override
            public void success(final ResponseApi api) {
                updateInformation(api);
            }

            @Override
            public void error(final Exception e) {
                Toast.makeText(getContext(), "City name is invalide", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateInformation(ResponseApi data) {
        final MainParent temp = data.getList().get(0);
        List<Weather> weatherList = temp.getWeatherList();
        int tempk = (int) temp.getMain().getTemp();
        int temps = (tempk - 273);
        String main = weatherList.get(0).getMain();
        String icon = weatherList.get(0).getIcon();
        String imageUrl = "http://openweathermap.org/img/w/" + icon + ".png";

        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("temp", temps);
        bundleArgs.putString("main", main);

        textView.setText(String.format("%d°", tempk));
        textView2.setText(main);
        textView3.setText(data.getCity().getName());
        Picasso.get().load(imageUrl).into(imgview);
    }
}

