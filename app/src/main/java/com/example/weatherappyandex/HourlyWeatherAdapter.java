package com.example.weatherappyandex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {
    private List<HourlyWeather> hourlyWeatherList;
    public HourlyWeatherAdapter(List<HourlyWeather> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_weather_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyWeather hourlyWeather = hourlyWeatherList.get(position);
        holder.hoursTextView.setText(hourlyWeather.getTime());
        holder.gradusTextView.setText(hourlyWeather.getTemperature());
    }
    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hoursTextView;
        public TextView gradusTextView;
        public ViewHolder(View view) {
            super(view);
            hoursTextView = view.findViewById(R.id.hours);
            gradusTextView = view.findViewById(R.id.gradus);
        }
    }
}