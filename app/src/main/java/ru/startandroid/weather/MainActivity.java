package ru.startandroid.weather;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    static final String TAG = "myLogs";
    ViewPager pager;
    PagerAdapter pagerAdapter;

    public MainActivity() throws IOException {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        Log.d(TAG, "Pajer setet Adapter");
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
//    final Intent intent = new Intent(this, FragmentWeather.class);
//    Bundle bundle = new Bundle();


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        List<Integer> cites = new ArrayList<>();



        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);


//            bundle.putIntegerArrayList("City", (ArrayList<Integer>) cites);
//            intent.putExtras(bundle);
//            startActivity(intent);
            cites.add(1217662);
            cites.add(1513157);
            cites.add(1216265);
            cites.add(1512569);
        }
        @Override
        public Fragment getItem(int position) {

                        int city = cites.get(position);
                        return FragmentWeather.newInstance(city);
        }
        @Override
        public int getCount() {
            return cites.size();
        }
    }


}




