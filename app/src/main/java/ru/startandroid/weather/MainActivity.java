package ru.startandroid.weather;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textView, textView2, textView3;
    ImageView imgview;
    final String LOG_TAG = "myLogs";
    String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/forecast?id=1512440&APPID=a7dc109561ec63ddd24cd4df691e3043";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        imgview = (ImageView) findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
                Log.d(LOG_TAG, "T is started");
            }
        });
    }
    Thread t = new Thread(new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            Log.d(LOG_TAG, "Thread is run!");
            try {
                URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/forecast?id=1512440&APPID=" +
                        "a7dc109561ec63ddd24cd4df691e3043"));
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();
                Log.d(LOG_TAG, "Conection is open");

                connection.addRequestProperty("a7dc109561ec63ddd24cd4df691e3043",
                        getString(R.string.open_weather_maps_app_id));
                Log.d(LOG_TAG, "RequestProperty");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null) {
                    json.append(tmp).append("\n");
                }
                reader.close();
                Log.d(LOG_TAG, json.toString());
                JSONObject data = new JSONObject(json.toString());
                if (data.getInt("cod") != 200) {
                    Log.d(LOG_TAG, "Returned null");
                    return;
                }
                JSONArray list = data.getJSONArray("list");
                JSONObject firstItemoftheArray = list.getJSONObject(0);
                JSONObject main = firstItemoftheArray.getJSONObject("main");
                JSONObject city = data.getJSONObject("city");
                final String name = city.getString("name");
                final double temp = main.getDouble("temp");
                final String temp2 = String.valueOf(temp);
                Double tempD = Double.parseDouble(temp2);
                final int tempI = (int) (tempD - 273.15);
                final String tempC = Integer.toString(tempI) + "°c";
                JSONObject weather = firstItemoftheArray.getJSONArray("weather").getJSONObject(0);
                String mainString = weather.getString("main");
                Log.d(LOG_TAG, (String) weather.get("main"));
                final String desc = weather.getString("description");
                Log.d(LOG_TAG, (String) weather.get("description"));
                final String icond = weather.getString("icon");
                Log.d(LOG_TAG, weather.getString("icon"));
                final String iconUrls = "http://openweathermap.org/img/w/" + icond + ".png";
                Log.d(LOG_TAG, (String) weather.get("icon"));
                connection.disconnect();
                Log.d(LOG_TAG, "Disconnect");
                byte[] bytes = imageByter(this, iconUrls);
                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(tempC);
                        textView2.setText(desc);
                        textView3.setText(name);
                        imgview.setImageBitmap(bm);
                    }
                });

            } catch (Exception e) {
                Log.e(LOG_TAG, "Exeption", e);
            }
        }
        private byte[] imageByter (Runnable context, String url){
            URL url1 = null;
            try {
                url1 = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream is = null;
            try {
                is = (InputStream) url1.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = is.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output.toByteArray();
        }
    });
}