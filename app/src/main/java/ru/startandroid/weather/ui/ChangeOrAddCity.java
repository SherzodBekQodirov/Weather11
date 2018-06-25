package ru.startandroid.weather.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;


import android.widget.Toast;
import ru.startandroid.weather.R;
import ru.startandroid.weather.data.CityFetcher;
import ru.startandroid.weather.data.ResponseApi;
import ru.startandroid.weather.data.ResponseListener;

public class ChangeOrAddCity extends Activity {

    private final CityFetcher cityFetcher = CityFetcher.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cange_cites);

        final SearchView searchView = (SearchView) findViewById(R.id.idsearch);
        final View progress = findViewById(R.id.progressView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                progress.setVisibility(View.VISIBLE);

                cityFetcher.fetchCity(query, new ResponseListener() {
                    @Override
                    public void success(final ResponseApi api) {
                        // Show any UI if activity is not finishing now
                        if (isFinishing()) {
                            return;
                        }
                        Toast.makeText(ChangeOrAddCity.this,
                                String.format("Requested city found and will be added to your list"),
                                Toast.LENGTH_SHORT).show();

                        progress.setVisibility(View.GONE);
                        Intent intent = new Intent();
                        intent.putExtra("name", api.getCity().getName());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void error(final Exception e) {
                        // Show any UI if activity is not finishing now
                        if (isFinishing()) {
                            return;
                        }
                        progress.setVisibility(View.GONE);
                        Toast.makeText(ChangeOrAddCity.this,
                                String.format("The searched city with a name %s not found", query),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

    }
}
