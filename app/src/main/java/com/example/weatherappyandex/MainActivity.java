package com.example.weatherappyandex;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

public class MainActivity extends AppCompatActivity {
    Weather weather = new Weather();
    private String tempValue;
    private String condition;
    ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetWeather getWeather = new GetWeather();
        getWeather.execute();
    }
    class GetWeather extends AsyncTask<Void, Void, Void>
    {
        String body;
        @Override
        protected Void doInBackground(Void... params)
        {
            Document d_doc = null;
            try {
                d_doc = Jsoup.connect("https://api.weather.yandex.ru/v2/forecast?lat=58.0105&lon=56.2502")
                        .header("X-Yandex-API-Key", "demo_yandex_weather_api_key_ca6d09349ba0")
                        .ignoreContentType(true).get();
                body = d_doc.text();
            }catch (Exception e){
                e.printStackTrace();
            }
            if (body != null) {
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    JSONArray forecasts = jsonObject.getJSONArray("forecasts");
                    for (ArrayList<HourlyWeather> weatherList : weather.hourlyWeatherData) {
                        weatherList.clear();
                    }
                    for (int i = 0; i < forecasts.length(); i++) {
                        JSONObject dailyForecast = forecasts.getJSONObject(i);
                        JSONArray hours = dailyForecast.getJSONArray("hours");
                        int sumTemp = 0;
                        int count = 0;
                        for (int j = 0; j < hours.length(); j++) {
                            JSONObject hour = hours.getJSONObject(j);
                            int temp = hour.getInt("temp");
                            sumTemp += temp;
                            count++;
                            String time = hour.getString("hour") + ":00";
                            String condition = hour.getString("condition");
                            weather.hourlyWeatherData.get(i).add(new HourlyWeather(time, temp + "°C"));
                        }
                        if (i == 1) {
                            weather.mondayAvgTemp = (sumTemp / count) + "°C";
                        } else if (i == 2) {
                            weather.tuesdayAvgTemp = (sumTemp / count) + "°C";
                        }
                    }
                    addweather();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(body);
                TextView textViewMonday = findViewById(R.id.tvConditionMonday);
                TextView textViewTuesday = findViewById(R.id.tvConditionTuesday);
                TextView textView = findViewById(R.id.tvTemperature);
                TextView textView2 = findViewById(R.id.tvCondition);
                TextView textView2WeatherHous = findViewById(R.id.tvTemperature2);
                TextView textView2ConditionHous = findViewById(R.id.tvCondition2);
                tempValue = jsonObject.getJSONObject("fact").getString("temp") + "°C";
                condition = jsonObject.getJSONObject("fact").getString("condition");
                textView.setText(tempValue);
                textView2.setText(condition);
                textView2WeatherHous.setText(tempValue);
                textView2ConditionHous.setText(condition);


                textViewMonday.setText(condition);
                textViewTuesday.setText(condition);
                TextView mondayAvgTextView = findViewById(R.id.monday_avg_temp);
                TextView tuesdayAvgTextView = findViewById(R.id.tuesday_avg_temp);
                mondayAvgTextView.setText(weather.mondayAvgTemp);
                tuesdayAvgTextView.setText(weather.tuesdayAvgTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setWeather(View view)
    {
        setContentView(R.layout.weatherhous);
        addweather2();
        addspiner();
        addHourlyWeather(0);
    }
    public void addweather2()
    {
        TextView textView2WeatherHous = findViewById(R.id.tvTemperature2);
        TextView textView2ConditionHous = findViewById(R.id.tvCondition2);
        textView2WeatherHous.setText(tempValue);
        textView2ConditionHous.setText(condition);
    }
    public void addweather()
    {
        TextView textView = findViewById(R.id.tvTemperature);
        TextView textView2 = findViewById(R.id.tvCondition);
        textView.setText(weather.temp);
        textView2.setText(weather.could);
        TextView mondayAvgTextView = findViewById(R.id.monday_avg_temp);
        TextView tuesdayAvgTextView = findViewById(R.id.tuesday_avg_temp);
        mondayAvgTextView.setText(weather.mondayAvgTemp);
        tuesdayAvgTextView.setText(weather.tuesdayAvgTemp);
    }
    public void addspiner() {
        String[] add = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, add);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addHourlyWeather(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Spinner", "No item selected");
            }
        });
    }
    public void addHourlyWeather(int dayIndex) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        HourlyWeatherAdapter adapter = new HourlyWeatherAdapter(weather.hourlyWeatherData.get(dayIndex));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }
    public void Back(View view)
    {
        setContentView(R.layout.activity_main);
        GetWeather getWeather = new GetWeather();
        getWeather.execute();
    }
}