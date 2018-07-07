package ru.startandroid.weather.ui.main;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import ru.startandroid.weather.R;
import ru.startandroid.weather.data.CityFetcher;
import ru.startandroid.weather.data.response.ResponseApi;
import ru.startandroid.weather.data.ResponseListener;
import ru.startandroid.weather.data.response.MainParent;
import ru.startandroid.weather.data.response.Weather;
import ru.startandroid.weather.ui.NotifyModel;

/**
 * Created by sher on 5/13/18.
 */
public class WeatherFragment extends Fragment {

    private static final String EXTRA_CITY_NAME = "city_name";
    private CityFetcher mCityFetcher = CityFetcher.getInstance();
    private String cityName;
    private FloatingActionButton btn1;
    private TextView textView, textView2, textView3, humidityView, speedView,
            seaView, populationView, maxTempView, minTempView, pressureView, geolatView, geolonView;
    private ImageView imgview;
    int temps;
    String nameCity;
    String main;
    public static final int NOTIFICATION_ID = 1;
    private ResponseApi data;

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
        humidityView = (TextView) v.findViewById(R.id.humidity);
        speedView = (TextView) v.findViewById(R.id.speed);
        seaView = (TextView) v.findViewById(R.id.sea);
        populationView = (TextView) v.findViewById(R.id.population);
        maxTempView = (TextView) v.findViewById(R.id.maxtemp);
        minTempView = (TextView) v.findViewById(R.id.mintemp);
        pressureView = (TextView) v.findViewById(R.id.pressure);
        geolatView = (TextView) v.findViewById(R.id.geolat);
        geolonView = (TextView) v.findViewById(R.id.geolon);

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

    private void fetchData() {
        mCityFetcher.fetchCity(cityName, new ResponseListener() {
            @Override
            public void success(final ResponseApi api) {
                   data = api;
                   Log.d("fetchData", "success " + data);
                   storeMainParents();
                   updateInformation();

            }

            @Override
            public void error(final Exception e) {
                Toast.makeText(getContext(), "Network error or City name is invalide", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeNotifyModel() {
        NotifyModel nm = new NotifyModel();
        nm.setCityName(cityName);
        nm.setMain(main);
        nm.setTemperature(temps);
        nm.setIcon(mBitmap);
        ((MainActivity)getActivity()).setNotifyModel(nm);
    }

    private void updateInformation() {
        final MainParent temp = data.getList().get(0);
        Long unixDateTime =  temp.getDt();
        Log.d("unix", temp.getDt().toString());
        List<Weather> weatherList = temp.getWeatherList();
        int humidity = temp.getMain().getHumidity();
        double speed = temp.getWind().getSpeed();
        double sea = temp.getMain().getSea_level();
        long population = data.getCity().getPopulation();
        int maxTemp = (int) temp.getMain().getTemp_max();
        Log.d("temp", String.valueOf(temp.getMain().getTemp_max()));
        Log.d("temp", String.valueOf(temp.getMain().getTemp_min()));
        int minTemp = (int) temp.getMain().getTemp_min();
        double pressure = temp.getMain().getPressure();
        double geolat = data.getCity().getCoord().getLat();
        double geolon = data.getCity().getCoord().getLon();
        int tempk = (int) temp.getMain().getTemp();
        temps = (tempk - 273);
        main = weatherList.get(0).getMain();
        String icon = weatherList.get(0).getIcon();

        String imageUrl = "http://openweathermap.org/img/w/" + icon + ".png";

        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("temp", temps);
        bundleArgs.putString("main", main);

        textView.setText(String.format("%d°", temps));
        textView2.setText(main);
        textView3.setText(data.getCity().getName());
        humidityView.setText(String.valueOf(humidity) + "%");
        speedView.setText(String.valueOf(speed) + " meter/sec");
        seaView.setText(String.valueOf(sea) + " \thPa\t");
        populationView.setText(String.valueOf(population));
        maxTempView.setText(String.format("%d°", maxTemp - 273));
        minTempView.setText(String.format("%d°", minTemp - 273));
        pressureView.setText(String.valueOf(pressure) + " hPa");
        geolatView.setText(String.valueOf("lat:" + geolat));
        geolonView.setText(String.valueOf("lon:" + geolon));
        Picasso.get().load(imageUrl).into(imgview);
        Picasso.get().load(imageUrl).into(target);
        nameCity = data.getCity().getName();

    }


    private Bitmap mBitmap;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mBitmap = bitmap;
            storeNotifyModel();
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    public List<MainParent> getMainParents(){
        return data.getList();
    }

    private void storeMainParents(){
        ((MainActivity)getActivity()).setMainParents(data.getList());
    }
}


