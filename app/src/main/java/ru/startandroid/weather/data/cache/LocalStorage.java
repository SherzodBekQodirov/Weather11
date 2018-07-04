package ru.startandroid.weather.data.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// This class stores and reads data from the disk on the caller thread. This is a sport for improvement. Make it concurrent.
public class LocalStorage {

    public static final String CITIES = "cities";

    private final SharedPreferences sharedPreferences;

    public LocalStorage(@NonNull final Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public List<String> readStoredCities(){
        final Set<String> cities = sharedPreferences.getStringSet(CITIES, new HashSet<String>());
        final List<String> result = new ArrayList<>(cities);
        Collections.sort(result);
        return result;
    }

    public void saveCityList(@NonNull final List<String > cityList) {
        sharedPreferences.edit().putStringSet(CITIES, new HashSet<String>(cityList)).commit();
    }
}
