package ru.startandroid.weather.ui.detail;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.startandroid.weather.R;
import ru.startandroid.weather.data.response.MainParent;

public class WeatherDetailsAdapter extends RecyclerView.Adapter<WeatherDetailsViewHolder> {

    private final Context mContext;
    private List<MainParent> list = new ArrayList<>();
    View view;

    public WeatherDetailsAdapter(Context context) {
        this.mContext = context;
    }

    public void accept(List<MainParent> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public WeatherDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_weather_details, parent, false);

            return new WeatherDetailsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(WeatherDetailsViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
