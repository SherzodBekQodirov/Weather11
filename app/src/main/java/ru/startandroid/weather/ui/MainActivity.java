package ru.startandroid.weather.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import java.util.Calendar;
import java.util.List;
import ru.startandroid.weather.R;
import ru.startandroid.weather.data.CityFetcher;

public class MainActivity extends AppCompatActivity {

    public static final int CITY_REQUEST_CODE = 705;

    private ViewPager viewPager;

    private MyFragmentPagerAdapter pagerAdapter;

    private LocalStorage localStorage;


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
                Intent t = new Intent(this, ChangeOrAddCity.class);
                startActivityForResult(t, MainActivity.CITY_REQUEST_CODE);
                return true;
            case R.id.addcity:
                Intent intent = new Intent(this, ChangeOrAddCity.class);
                this.startActivityForResult(intent, MainActivity.CITY_REQUEST_CODE);
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                return true;
            case R.id.abauttheprogram:
                Intent intent1 = new Intent(this, AboutActivity.class);
                this.startActivityForResult(intent1, MainActivity.CITY_REQUEST_CODE);
                return true;
            case R.id.action_calendar:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new OnDateSetListener() {
                            @Override
                            public void onDateSet(final DatePickerDialog view, final int year, final int monthOfYear,
                                    final int dayOfMonth) {
                                final CityFetcher instance = CityFetcher.getInstance();
                                // TODO Sherxon shu yerda datani bervoringda shu data buyicha hamma datani yangilasin
                                // instance.setDate();
                                pagerAdapter.notifyDataSetChanged();
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
    protected void onDestroy() {
        super.onDestroy();
        List<String> cityList = pagerAdapter.getCityList();
        localStorage.saveCityList(cityList);
    }

    private void showDeleteDialog() {
        int index = viewPager.getCurrentItem();
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
            pagerAdapter.addCity(name);
            pagerAdapter.notifyDataSetChanged();
        }
    }

//    public void showNotification() {
//         NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//            builder.setSmallIcon(R.drawable.sunnywhite);
//            builder.setAutoCancel(true);
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                builder.setLargeIcon(mBitmap);
//                builder.setColor(getResources().getColor(R.color.color0));
//            } else {
//                builder.setSmallIcon(R.drawable.ic_launcher_round);
//            }
//            builder.setContentTitle(nameCity);
//            builder.setContentText(main);
//            builder.setSubText("");
//            builder.setStyle(new NotificationCompat.BigTextStyle()
//                    .bigText(temps + "°"));
//
//            Intent targetIntent = new Intent(getContext(), MainActivity.class);
//            targetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            PendingIntent contentIntent = PendingIntent
//                    .getActivity(getContext(), 0, targetIntent, PendingIntent.FLAG_ONE_SHOT);
//            builder.setContentIntent(contentIntent);
//
//            NotificationManager notificationManager = (NotificationManager) this.getSystemService(
//                    NOTIFICATION_SERVICE);
//            if (notificationManager != null)
//                notificationManager.notify(NOTIFICATION_ID, builder.build());
//        }
//    }
}




