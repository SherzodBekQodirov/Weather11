package ru.startandroid.weather;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.startandroid.weather.optionsmenuitems.ChangeOrAddCity;
import ru.startandroid.weather.response.MainParent;
import ru.startandroid.weather.response.Weather;

import static android.content.Context.NOTIFICATION_SERVICE;
import static ru.startandroid.weather.MainActivity.NOTIFICATION_ID;

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
    final int DIALOG_DEL_CITY = 1;
    int pageNumber;
    String imageUrl;
    private String cityName;


    static FragmentWeather newInstance(String city) {
        FragmentWeather pageFragment = new FragmentWeather();
        Bundle bundle = new Bundle();
        bundle.putString("city", city);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityName = getArguments().getString("city");
        setHasOptionsMenu(true);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment, null);
        intiViews(v);
        refreshBtnClickListner();

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void refreshBtnClickListner() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                imgview.startAnimation(animation);
                updateViewPager();
                showNotification(null);
            }
        });
    }

    private void intiViews(View v) {
        btn1 = (FloatingActionButton) v.findViewById(R.id.floatingActionButton2);
        textView = (TextView) v.findViewById(R.id.textView);
        textView2 = (TextView) v.findViewById(R.id.textView2);
        textView3 = (TextView) v.findViewById(R.id.textView3);
        imgview = (ImageView) v.findViewById(R.id.imageView);
    }

    private void loadData() {
        final String url =
                "http://api.openweathermap.org/data/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(url + DEFAULT_VERSION + "/")
                .build();
        final Link link = retrofit.create(Link.class);
        Call<ResponseApi> call = link.getData(cityName, "a7dc109561ec63ddd24cd4df691e3043");
        call.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(final Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "response" + response.body().toString());
                } else {
                    try {
                        Log.d(LOG_TAG, "response full errors" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                final ResponseApi myResponse = response.body();
                if (myResponse == null) {
                    Toast.makeText(getContext(), "Город был ошибочно идентифицирован или такого города не было найдено", Toast.LENGTH_LONG).show();
                    return;
                }
                final MainParent temp = myResponse.list.get(0);
                List<Weather> weatherList = temp.getWeatherList();
                int tempk = (int) temp.getMain().getTemp();
                final int temps = (tempk - 273);
                final String main = weatherList.get(0).getMain();
                String icon = weatherList.get(0).getIcon();
                imageUrl = "http://openweathermap.org/img/w/" + icon + ".png";

                Bundle bundleArgs = new Bundle();
                bundleArgs.putString("temp", String.valueOf(temps));
                bundleArgs.putString("namecity", main); //

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(String.valueOf(temps) + "°");
                        textView2.setText(main);
                        textView3.setText(myResponse.city.getName());
                        Picasso.get().load(imageUrl).into(imgview);
                    }
                });

            }
            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                //agarda zapros serverga yetib bormasa
            }
        });
    }

    private void loadIconAndShowNotifaction() {
        Picasso.get().load(imageUrl).into(target);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changecity:
                setIsChange(true);
                Intent t = new Intent(getActivity(), ChangeOrAddCity.class);
                getActivity().startActivityForResult(t, MainActivity.CITY_REQUEST_CODE);
                return true;
            case R.id.addcity:
                setIsChange(false);
                Intent intent = new Intent(getActivity(), ChangeOrAddCity.class);
                getActivity().startActivityForResult(intent, MainActivity.CITY_REQUEST_CODE);
                return true;
            case R.id.action_delete:
                onCreateDialog(DIALOG_DEL_CITY).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Dialog onCreateDialog(int id) {
        if (id == DIALOG_DEL_CITY) {
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setTitle(R.string.exit);
            adb.setMessage(R.string.save_data);
            adb.setIcon(android.R.drawable.ic_dialog_info);
            adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteCity();
                }
            });
            adb.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            adb.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            return adb.create();
        }
        return onCreateDialog(id);
    }

    private void setIsChange(boolean isChange) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setIsChange(isChange);
        }
    }

    private void deleteCity() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).deleteCity();
        }
    }

    private void updateViewPager() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateViewPager();
        }
    }



    public void showNotification(Bitmap bitmap){ // new proekt sozdat qilib. sekin sekin kopiy past qivorasiz ok hop bro
         if(getActivity() instanceof  MainActivity){
            String cityName;
            Bundle extras = getArguments();
            cityName = extras.getString("namecity");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
            builder.setSmallIcon(R.drawable.ic_launcher_round);
            builder.setAutoCancel(true);
            builder.setLargeIcon(bitmap);
            builder.setContentTitle(cityName);
            builder.setContentText("Time to learn about notifications!");
            builder.setSubText("Tap to view documentation about notifications.");
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());


        }
    }




    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("onBitmapLoaded", "bla bla");
            showNotification(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}

