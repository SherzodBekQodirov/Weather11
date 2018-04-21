package ru.startandroid.weather;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import ru.startandroid.weather.ImageByter;
public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textView, textView2, textView3;
    ImageView imgview;
    MyTask mt;
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
                mt = new MyTask();
                mt.execute();

                Log.d(LOG_TAG, "T is started");
            }
        });
    }

    class MyTask extends AsyncTask<Void, Void, Void> {
        String tempC, desc, name;
        Bitmap bitmap;

        @Override
        protected Void doInBackground(Void... voids) {
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
                }
                JSONArray list = data.getJSONArray("list");
                JSONObject firstItemoftheArray = list.getJSONObject(0);
                JSONObject main = firstItemoftheArray.getJSONObject("main");
                JSONObject city = data.getJSONObject("city");
                this.name = city.getString("name");
                final double temp = main.getDouble("temp");
                final String temp2 = String.valueOf(temp);
                Double tempD = Double.parseDouble(temp2);
                final int tempI = (int) (tempD - 273.15);
                this.tempC = Integer.toString(tempI) + "°c";
                JSONObject weather = firstItemoftheArray.getJSONArray("weather").getJSONObject(0);
                String mainString = weather.getString("main");
                Log.d(LOG_TAG, (String) weather.get("main"));
                this.desc = weather.getString("description");
                Log.d(LOG_TAG, (String) weather.get("description"));
                final String icond = weather.getString("icon");
                Log.d(LOG_TAG, weather.getString("icon"));
                final String iconUrls = "http://openweathermap.org/img/w/" + icond + ".png";
                Log.d(LOG_TAG, (String) weather.get("icon"));
                connection.disconnect();
                Log.d(LOG_TAG, "Disconnect");
                this.bitmap = ImageByter.creatBitmap(iconUrls);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Exeption", e);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            textView.setText(tempC);
            textView2.setText(desc);
            textView3.setText(name);
            imgview.setImageBitmap(bitmap);
        }
    }

}
