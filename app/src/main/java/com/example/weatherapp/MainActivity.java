package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.model.WeatherResponse;
import com.example.weatherapp.retrofit.RetrofitClient;
import com.example.weatherapp.retrofit.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Weather";

    public String lat = "39";
    public String lon = "121";
    public String appid = "a485dd6136c40618fe3ea77cbf4027fb";


    CurrentWeather currentWeatherFragment;
    Forecast weatherForecastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      currentWeatherFragment = (CurrentWeather) getSupportFragmentManager().findFragmentById(R.id.current_weather_fragment);
      weatherForecastFragment = (Forecast) getSupportFragmentManager().findFragmentById(R.id.forecast_fragment);

        fetchCurrentWeather();
    }

    public void fetchCurrentWeather() {

        // 1: Declare Service and Initialize it using RetrofitClient
        WeatherService weatherService =
                RetrofitClient
                        .getRetrofit()
                        .create(WeatherService.class);

        // 2: Declare Service Return type and initialize it using the Service
        Call<WeatherResponse> weatherCall = weatherService.getWeather(lat, lon, appid);

        // 3: Use the Call from Step 2 and call the .enqueue method
        weatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse( Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Success");
                    //TODO implement RecyclerView
                    // loadRecyclerView(response.body());
                } else {
                    Log.d(TAG, "onResponse: Failure");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Log.d(TAG, "onResponse: Failure");

            }
        });
    }
}
