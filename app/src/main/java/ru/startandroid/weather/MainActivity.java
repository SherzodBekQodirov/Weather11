package ru.startandroid.weather;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.startandroid.weather.optionsmenuitems.ChangeOrAddCity;


public class MainActivity extends AppCompatActivity {

    public static final int CITY_REQUEST_CODE = 705;
    static final String TAG = "myLogs";
    ViewPager pager;
    PagerAdapter pagerAdapter;
    boolean isChange;
    private List<String> cityList = new ArrayList<>();
    public static final int NOTIFICATION_ID = 1;



    public MainActivity() throws IOException {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager) findViewById(R.id.pager);
        fillCityList();
        initViewPager();
    }

    private void fillCityList(){
        cityList.add("Tashkent");
    }

    private void pagerItemClickListener() {
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
            }
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initViewPager(){
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pagerItemClickListener();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if(requestCode == CITY_REQUEST_CODE && resultCode == RESULT_OK){
            String name = data.getStringExtra("name");
            if(TextUtils.isEmpty(name)){
                return;
            }
            if(isChange){
                cityList.set(pager.getCurrentItem(), name);
            }else{
                cityList.add(name);
            }
            Log.d("onActivityResult", "cityList: "+cityList);
            updateViewPager();
        }
    }

    public void updateViewPager(){
        pagerAdapter.notifyDataSetChanged();
        pager.invalidate();
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
                        String city = cityList.get(position);
                        return FragmentWeather.newInstance(city);
        }
        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    public void deleteCity(){
        cityList.remove(pager.getCurrentItem());
        updateViewPager();
        if(cityList.isEmpty()){
            Intent intent = new Intent(this, ChangeOrAddCity.class);
            setIsChange(false);
            startActivityForResult(intent, CITY_REQUEST_CODE);
        }
    }

    public void setIsChange(boolean isChange){
        this.isChange = isChange;
    }

}




