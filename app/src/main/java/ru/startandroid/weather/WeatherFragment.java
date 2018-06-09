package ru.startandroid.weather;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import ru.startandroid.weather.data.CityFetcher;
import ru.startandroid.weather.data.ResponseListener;
import ru.startandroid.weather.optionsmenuitems.ChangeOrAddCity;
import ru.startandroid.weather.response.MainParent;
import ru.startandroid.weather.response.Weather;
import static android.content.Context.NOTIFICATION_SERVICE;
import static ru.startandroid.weather.MainActivity.NOTIFICATION_ID;
/**
 * Created by sher on 5/13/18.
 */
public class WeatherFragment extends Fragment {
    private FloatingActionButton btn1;
    private TextView textView, textView2, textView3;
    private ImageView imgview;
    private Bitmap bitmap;
    final String LOG_TAG = "myLogs";
    long id;

    final int DIALOG_DEL_CITY = 1;
    int pageNumber;
    String imageUrl;
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String cityName;
    public String nameCity;
    private int temps;
    private String main;

    public CityFetcher mCityFetcher;

    static WeatherFragment newInstance(String city) {
        WeatherFragment pageFragment = new WeatherFragment();
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
        mCityFetcher = new CityFetcher();
        mCityFetcher.setListener(listener);
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

    @Override
    public void onDestroyView() {
        mCityFetcher.onDestroy();
        super.onDestroyView();
    }

    private void refreshBtnClickListner() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                imgview.startAnimation(animation);
                updateViewPager();
                Picasso.get().load(imageUrl).into(target);
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
    public void loadData() { // endi bu zaprosni activity ga chiqarish kerak, o''ziz harakat qib ko'ring.
        mCityFetcher.fetchCity(cityName);

    }



    private ResponseListener listener = new ResponseListener() {
        @Override
        public void success(final ResponseApi api) {

            final MainParent temp = api.list.get(0);
            List<Weather> weatherList = temp.getWeatherList();
            int tempk = (int) temp.getMain().getTemp();
            temps = (tempk - 273);
            main = weatherList.get(0).getMain();
            String icon = weatherList.get(0).getIcon();
            imageUrl = "http://openweathermap.org/img/w/" + icon + ".png";
            nameCity = api.city.getName();

            Bundle bundleArgs = new Bundle();
            bundleArgs.putInt("temp", temps);
            bundleArgs.putString("main", main);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(String.valueOf(temps) + "°");
                    textView2.setText(main);
                    textView3.setText(api.city.getName());
                    Picasso.get().load(imageUrl).into(imgview);
                }
            });
        }

        @Override
        public void error(Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    };

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
    public void deleteCity() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onDeleteCurrentCityClick();
        }
    }
    private void updateViewPager() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateViewPager();
        }
    }

    public void showNotification(){
         if(getActivity() instanceof  MainActivity){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
            builder.setSmallIcon(R.drawable.sunnywhite);
            builder.setAutoCancel(true);
             if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                 builder.setLargeIcon(mBitmap);
                 //Log.d(LOG_TAG, "bitmap is=" +bitmap);
                 builder.setColor(getResources().getColor(R.color.color0));
             } else {
                 builder.setSmallIcon(R.drawable.ic_launcher_round);
             }
            builder.setContentTitle(nameCity);
            builder.setContentText(main);
            builder.setSubText("SubText");
             builder.setStyle(new NotificationCompat.BigTextStyle()
                     .bigText(temps+"°"));

            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private Bitmap mBitmap;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("onBitmapLoaded", "bla bla");
            mBitmap = bitmap;
            showNotification();
        }
        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        }
        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }

    };

    public interface Callbacks {
        void showNotification(Notification notification);

    }

}

