package ru.startandroid.weather.ui.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import ru.startandroid.weather.R;
import ru.startandroid.weather.data.response.MainParent;
import ru.startandroid.weather.ui.AboutActivity;
import ru.startandroid.weather.ui.ChangeOrAddCity;
import ru.startandroid.weather.data.cache.LocalStorage;
import ru.startandroid.weather.ui.NotifyModel;
import ru.startandroid.weather.ui.base.BaseActivity;
import ru.startandroid.weather.ui.detail.WeatherDetailsActivity;
import ru.startandroid.weather.util.DateUtils;

import static ru.startandroid.weather.ui.main.WeatherFragment.NOTIFICATION_ID;

public class MainActivity extends BaseActivity {

    public static final int CITY_REQUEST_CODE = 705;

    private ViewPager viewPager;

    private MyFragmentPagerAdapter pagerAdapter;
    private LocalStorage localStorage;
    boolean isChange;
    int index;
    private NotifyModel notifyModel;
    private List<MainParent> mainParents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localStorage = new LocalStorage(this);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        final List<String> strings = localStorage.readStoredCities();
        for (String city : strings) {
            pagerAdapter.addCity(city);
        }
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changecity:
                setIsChange(true);
                Intent t = new Intent(this, ChangeOrAddCity.class);
                startActivityForResult(t, MainActivity.CITY_REQUEST_CODE);
                return true;
            case R.id.addcity:
                setIsChange(false);
                Intent intent = new Intent(this, ChangeOrAddCity.class);
                this.startActivityForResult(intent, MainActivity.CITY_REQUEST_CODE);
                return true;
            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                return true;
            case R.id.abauttheprogram:
                Intent intent1 = new Intent(this, AboutActivity.class);
                this.startActivityForResult(intent1, MainActivity.CITY_REQUEST_CODE);
                return true;
            case R.id.action_calendar:
                    final Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            new OnDateSetListener() {
                                @Override
                                public void onDateSet(final DatePickerDialog view, final int year, final int monthOfYear,
                                                      final int dayOfMonth) {
                                    Calendar selected = Calendar.getInstance();
                                    selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    selected.set(Calendar.MONTH, monthOfYear);
                                    selected.set(Calendar.YEAR, year);

                                    List<MainParent> list = sortedWeather(selected.getTime());
                                    if (!list.isEmpty()) {
                                        startActivity(WeatherDetailsActivity.getIntent(MainActivity.this, list));
                                    }else {
                                        Toast.makeText(getApplicationContext(), "We have only 5 days information", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            },
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)

                    );

                    dpd.show(getFragmentManager(), "Datepickerdialog");
                    break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        showNotification();
        super.onDestroy();
        Log.d("MainActivity","onDestroy() ");

        storeCities();
    }


    private List<MainParent> sortedWeather(Date date){
        List<MainParent> sortedWeathers = new ArrayList<>();
        for(MainParent mp: mainParents){
            if(DateUtils.isTheSameDay(mp.getDate(), date)){
                sortedWeathers.add(mp);
            }
        }
        return sortedWeathers;
    }

    public void setNotifyModel(NotifyModel nm){
        notifyModel = nm;
    }

    private void storeCities() {
        List<String> cityList = pagerAdapter.getCityList();
        localStorage.saveCityList(cityList);
    }

    private void showDeleteDialog() {
        index = viewPager.getCurrentItem();
        final List<String> cityList = pagerAdapter.getCityList();
        if (index >= cityList.size() || index < 0) {
            Toast.makeText(MainActivity.this, "Error during deleting the city", Toast.LENGTH_SHORT).show();
            return;
        }
        final String currentCity = cityList.get(index);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.exit);
        adb.setMessage(String.format(getString(R.string.save_data), currentCity));
        adb.setIcon(android.R.drawable.ic_dialog_info);
        adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pagerAdapter.removeCity(currentCity);
                pagerAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, String.format("The city %s deleted", currentCity),
                        Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == CITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            if (TextUtils.isEmpty(name)) {
                return;
            }
            if(isChange){
                pagerAdapter.setCityList().set(viewPager.getCurrentItem(), name);
            }else{
                pagerAdapter.addCity(name);
            }
            pagerAdapter.notifyDataSetChanged();
        }
    }
    public void setIsChange(boolean isChange){
        this.isChange = isChange;
    }

    public void setMainParents(List<MainParent> list){
        mainParents = list;
    }
    public void showNotification() {
        Log.d("MainActivity", "bitmap"+ notifyModel.getIcon());
         NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.sunnywhite);
            builder.setAutoCancel(true);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setLargeIcon(notifyModel.getIcon());
                builder.setColor(getResources().getColor(R.color.color0));
            } else {
                builder.setSmallIcon(R.drawable.ic_launcher_round);
            }
            builder.setContentTitle(notifyModel.getCityName());
            builder.setContentText(notifyModel.getMain());
            builder.setSubText("");
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(notifyModel.getTemperature() + "°"));

            Intent targetIntent = new Intent(this, MainActivity.class);
            targetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent
                    .getActivity(this, 0, targetIntent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(contentIntent);

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(
                    NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}




