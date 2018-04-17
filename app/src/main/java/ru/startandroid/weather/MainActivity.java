package ru.startandroid.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textView;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
                Log.d(LOG_TAG, "T is started");
            }
        });
        String OPEN_WEATHER_MAP_API =
                "http://api.openweathermap.org/data/2.5/forecast?id=1512440&APPID=a7dc109561ec63ddd24cd4df691e3043";
    }

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "Thread is run!");
            try {
                URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/forecast?id=1512440&APPID=" +
                        "a7dc109561ec63ddd24cd4df691e3043"));
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();
                Log.d(LOG_TAG, "Conection is open");

                connection.addRequestProperty("a7dc109561ec63ddd24cd4df691e3043",
                        getString(R.string.open_weather_maps_app_id));
                Log.d(LOG_TAG, "RequestProperty");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();
                Log.d(LOG_TAG, json.toString());
                JSONObject data = new JSONObject(json.toString());
                if(data.getInt("cod") != 200){
                    Log.d(LOG_TAG, "Returned null");


                }
                JSONObject jSOn
                connection.disconnect();
                Log.d(LOG_TAG, "Disconnect");


            }catch(Exception e){
                Log.e(LOG_TAG, "Exeption", e);
            }

        }
        });
    }

