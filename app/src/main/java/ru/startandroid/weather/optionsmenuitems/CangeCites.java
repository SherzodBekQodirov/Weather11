package ru.startandroid.weather.optionsmenuitems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;



import ru.startandroid.weather.R;

public class CangeCites extends Activity {
    String searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cange_cites);
        SearchView searchView = (SearchView) findViewById(R.id.idsearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        Intent intent = new Intent(this, Activity.class);
        intent.putExtra("searchView", searchView.getQuery());
        startActivity(intent);
    }

}
