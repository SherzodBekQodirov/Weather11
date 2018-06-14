package ru.startandroid.weather.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;



import ru.startandroid.weather.R;

public class ChangeOrAddCity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cange_cites);
        final SearchView searchView = (SearchView) findViewById(R.id.idsearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent();
                intent.putExtra("name", query);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

    }

}
