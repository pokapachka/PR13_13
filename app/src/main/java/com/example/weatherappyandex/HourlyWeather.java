package com.example.weatherappyandex;

public class HourlyWeather {
    private String time;
    private String temperature;
    public HourlyWeather(String time, String temperature) {
        this.time = time;
        this.temperature = temperature;
    }
    public String getTime() {
        return time;
    }
    public String getTemperature() {
        return temperature;
    }
}
