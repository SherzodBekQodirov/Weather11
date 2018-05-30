package ru.startandroid.weather;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.startandroid.weather.optionsmenuitems.ChangeOrAddCity;


public class MainActivity extends AppCompatActivity {

    public static final int CITY_REQUEST_CODE = 705;
    static final String TAG = "myLogs";
    private ViewPager viewPager;
    PagerAdapter pagerAdapter;
    boolean isChange;
    private List<Fragment> fragmentList = new ArrayList<>();
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        fillCityList();
        initViewPager();
    }

    private void fillCityList(){
        fragmentList.add(FragmentWeather.newInstance("Tashkent"));
    }

    private void pagerItemClickListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        pagerItemClickListener();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if(requestCode == CITY_REQUEST_CODE && resultCode == RESULT_OK){
            String name = data.getStringExtra("name");
            Fragment fragment = FragmentWeather.newInstance(name);
            if(TextUtils.isEmpty(name)){
                return;
            }
            if(isChange){
                fragmentList.set(viewPager.getCurrentItem(), fragment);
            }else{
                fragmentList.add(fragment);
            }
            Log.d("onActivityResult", "fragmentList: "+ fragmentList);
            updateViewPager();
        }
    }

    public void updateViewPager(){
        pagerAdapter.notifyDataSetChanged();
        viewPager.invalidate();
    }

    public void onDeleteCityClick(){
        deleteCurrentCity();
        updateViewPager();
        addCityIfEmpty();
    }

    private void deleteCurrentCity() {
        fragmentList.remove(viewPager.getCurrentItem());
    }

    private void addCityIfEmpty() {
        if(fragmentList.isEmpty()){
            Intent intent = new Intent(this, ChangeOrAddCity.class);
            setIsChange(false);
            startActivityForResult(intent, CITY_REQUEST_CODE);
        }
    }

    public void setIsChange(boolean isChange){
        this.isChange = isChange;
    }

}




