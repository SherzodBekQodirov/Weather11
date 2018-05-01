package ru.startandroid.weather;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.startandroid.weather.response.MainParent;
import ru.startandroid.weather.response.Weather;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private TextView textView, textView2, textView3;
    private ImageView imgview;
    private Bitmap bitmap;
    private final String LOG_TAG = "myLogs";

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
                getAsyncCall();
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                imgview.startAnimation(animation);
            }
        });
    }

    public void getAsyncCall() {
        String url =
                "http://api.openweathermap.org/data/2.5/forecast?id=1512440&APPID=a7dc109561ec63ddd24cd4df691e3043";
        final OkHttpClient httpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Gson gson = new Gson();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "Something went wrong. Check logs", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Error: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d(LOG_TAG, body);
                final ResponseApi responseApi = gson.fromJson(body, ResponseApi.class);
                Log.d(LOG_TAG, responseApi.toString());
                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }
                final MainParent temp = responseApi.list.get(0);
                List<Weather> weatherList = temp.getWeatherList();
                int tempk = (int) temp.getMain().getTemp();
                final int temps = (tempk - 273);
                final String city = responseApi.getCity().getName();
                final String main = weatherList.get(0).getMain();
                if (weatherList != null && !weatherList.isEmpty()) {
                    String icon = weatherList.get(0).getIcon();
                    if (!TextUtils.isEmpty(icon)) {
                        final String iconUrls = "http://openweathermap.org/img/w/" + icon + ".png";
                        bitmap = ImageByter.creatBitmap(iconUrls);
                    }
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(String.valueOf(temps) + "°C");
                        textView2.setText(main);
                        textView3.setText(city);
                        if (bitmap != null) {
                            imgview.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        });
    }
}