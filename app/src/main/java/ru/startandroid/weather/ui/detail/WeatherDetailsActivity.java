package ru.startandroid.weather.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import ru.startandroid.weather.R;
import ru.startandroid.weather.data.response.MainParent;
import ru.startandroid.weather.ui.base.BaseActivity;

public class WeatherDetailsActivity extends BaseActivity {

    private static final String EXTRA_MAIN_PARENTS = " extra main parents";

    RecyclerView mRecyclerView;

    public static Intent getIntent(Activity a, List<MainParent> list){
        Intent i  = new Intent(a, WeatherDetailsActivity.class);
        i.putExtra(EXTRA_MAIN_PARENTS, (Serializable) list);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initWeatherDetails();
    }

    private void initWeatherDetails() {
        WeatherDetailsAdapter adapter = new WeatherDetailsAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        List<MainParent> list = (List<MainParent>) getIntent().getSerializableExtra(EXTRA_MAIN_PARENTS);
        Log.d("WeatherDetailsActivity", "mainParents "+list);
        adapter.accept(list);
    }
}
