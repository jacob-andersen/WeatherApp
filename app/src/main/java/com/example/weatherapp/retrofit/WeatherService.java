package com.example.weatherapp.retrofit;

import com.example.weatherapp.CurrentWeatherModel.WeatherResponse;
import com.example.weatherapp.WeatherForecastModel.WeatherForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    String appid="a485dd6136c40618fe3ea77cbf4027fb";

    @GET ("data/2.5/forecast")
    Call <WeatherForecastResponse> getForecast(@Query("id") int cityId, @Query("units") String unit, @Query("appid") String appid );
    @GET ("data/2.5/weather")
    Call <WeatherResponse> getWeather(@Query("lat") String lat, @Query("lon") String lon, @Query ("units") String units, @Query("APPID") String appid );
}
