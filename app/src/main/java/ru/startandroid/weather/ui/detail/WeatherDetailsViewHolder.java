package ru.startandroid.weather.ui.detail;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.startandroid.weather.R;
import ru.startandroid.weather.data.response.MainParent;
import ru.startandroid.weather.util.DateUtils;

public class WeatherDetailsViewHolder extends RecyclerView.ViewHolder {

    TextView tvMain;
    TextView tvTime;
    TextView tvTemp;
    ImageView imageView;



    public WeatherDetailsViewHolder(View itemView) {
        super(itemView);
        tvMain = (TextView) itemView.findViewById(R.id.main);
        tvTime = (TextView) itemView.findViewById(R.id.time);
        tvTemp = (TextView) itemView.findViewById(R.id.temp);
        imageView = (ImageView) itemView.findViewById(R.id.iconqwe);

    }

    @SuppressLint("DefaultLocale")
    public void bind(MainParent parent){
        tvMain.setText(parent.getWeatherList().get(0).main);
        tvTime.setText(DateUtils.getDateTimeString(parent.getDate()));
        int temp = (int)parent.getMain().getTemp() - 273;
        tvTemp.setText(String.format(temp+"°C"));
        String icon = parent.getWeatherList().get(0).getIcon();
        String imageUrl = "http://openweathermap.org/img/w/" + icon + ".png";
        Picasso.get().load(imageUrl).into(imageView);
    }
}
