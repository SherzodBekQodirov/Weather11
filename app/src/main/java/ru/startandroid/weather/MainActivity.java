package ru.startandroid.weather;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.startandroid.weather.response.MainParent;
import ru.startandroid.weather.response.Weather;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textView, textView2, textView3;
    ImageView imgview;
    Bitmap bitmap;
//    MyTask mt;
    final String LOG_TAG = "myLogs";
    OkHttpClient client;
    Request request;
    String url =
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
                try {

                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public class okhttp {
        public void getAsyncCall(){

            String url =
                    "http://api.openweathermap.org/data/2.5/forecast?id=1512440&APPID=a7dc109561ec63ddd24cd4df691e3043";

            final OkHttpClient httpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Gson gson = new Gson();
            httpClient.newCall(request).enqueue(new Callback() {


                @Override public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final ResponseApi responseApi = gson.fromJson(response.body().charStream(), ResponseApi.class);
                    Log.d(LOG_TAG, responseApi.toString());

                    if (!response.isSuccessful()) {
                        throw new IOException("Error response " + response);
                    }
//                    final List<Weather> icond = responseApi.getWeatherList();
//                    final String iconUrls = "http://openweathermap.org/img/w/" + icond + ".png";
//                    bitmap = ImageByter.creatBitmap(iconUrls);
                    final MainParent temp = responseApi.list.get(1);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText((CharSequence) temp);
//                            textView2.setText(name);
//                            textView3.setText(desc);
//                            imgview.setImageBitmap(bitmap);
                        }
                    });
                }
            });
        }


    }

}
