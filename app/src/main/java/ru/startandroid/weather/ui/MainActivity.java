package ru.startandroid.weather.ui;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import ru.startandroid.weather.R;
import ru.startandroid.weather.data.ResponseApi;
import ru.startandroid.weather.data.CityFetcher;
import ru.startandroid.weather.data.ResponseListener;

public class MainActivity extends AppCompatActivity implements WeatherFragment.Callbacks {

    public static final int CITY_REQUEST_CODE = 705;
    static final String TAG = "myLogs";
    private ViewPager viewPager;
    PagerAdapter pagerAdapter;
    boolean isChange = false;
    private List<Fragment> fragmentList = new ArrayList<>();
    public static final int NOTIFICATION_ID = 1;
    public CityFetcher mCityFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        attachCityFetcher();
        fetchCity();
    }

    private void attachCityFetcher() {
        mCityFetcher =  new CityFetcher();
        mCityFetcher.setListener(listener);
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        initViewPager();
    }

    private void fetchCity(){
        mCityFetcher.fetchCity("Tashkent");
    }

    private void pagerItemClickListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
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
            if(TextUtils.isEmpty(name)){
                return;
            }
            mCityFetcher.fetchCity(name);
        }
    }

    public void updateViewPager(){
        pagerAdapter.notifyDataSetChanged();
        viewPager.invalidate();
    }

    public void onDeleteCurrentCityClick(){
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

    @Override
    protected void onDestroy() {
        detachCityFetcher();
        tryToShowNotification();
        super.onDestroy();
    }

    private void detachCityFetcher() {
        mCityFetcher.onDestroy();
    }

    private void tryToShowNotification() {
        WeatherFragment f = (WeatherFragment) fragmentList.get(viewPager.getCurrentItem());
        f.showNotification();
    }

    @Override
    public void showNotification(Notification notification) {

    }

    private ResponseListener listener = new ResponseListener() {
        @Override
        public void success(final ResponseApi api) {
            Fragment fragment = WeatherFragment.newInstance(api);
            if(isChange){
                fragmentList.set(viewPager.getCurrentItem(), fragment);
            }else{
                fragmentList.add(fragment);
            }
            updateViewPager();
        }

        @Override
        public void error(Exception e) {
            Toast.makeText(MainActivity.this, "City name is invalide", Toast.LENGTH_SHORT).show();
        }
    };
}




