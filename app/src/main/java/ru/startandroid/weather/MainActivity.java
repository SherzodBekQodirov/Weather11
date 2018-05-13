package ru.startandroid.weather;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



import java.io.IOException;



public class MainActivity extends AppCompatActivity {



    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


}
}
