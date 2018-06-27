package ru.startandroid.weather.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> cityList = new ArrayList<>();

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        final String cityName = cityList.get(position);
        Fragment fragment = WeatherFragment.newInstance(cityName);
        return fragment;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return cityList.get(position);
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    /**
     * Adds the fragment into the list if it doesn't exist
     *
     * @param name The name of the city
     * @return whether or not fragment added
     */
    public boolean addCity(@NonNull final String name) {
        if (cityList.indexOf(name) > -1) {
            return false;
        } else {
            cityList.add(name);
            return true;
        }
    }

    /**
     * Removes the fragment from the list if it exists
     *
     * @param name The name of the city
     * @return index of the removed element
     */
    public int removeCity(@NonNull final String name) {
        final int index = cityList.indexOf(name);
        if (index < 0) {
            // This city doesn't exist in our list
        } else {
            cityList.remove(name);
        }
        return index;
    }


    public List<String> getCityList() {
        return cityList;
    }
    public List<String> setCityList() {
        return cityList;
    }
}
