package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.weatherapp.model.WeatherResponse;
import com.example.weatherapp.retrofit.RetrofitClient;
import com.example.weatherapp.retrofit.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Weather";

    TextView tvLocation;
    TextView tvTemp;
    TextView tvConditions;
    ImageView ivWeather;

    public WeatherResponse weatherResponse;
    public String lat = "39.952778";
    public String lon = "-75.163611";
    public String units = "imperial";
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
        final Call<WeatherResponse> weatherCall = weatherService.getWeather(lat, lon, units, appid);

        // 3: Use the Call from Step 2 and call the .enqueue method
        weatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: Success");
                    weatherResponse = response.body();
                    populateFragments();

                    String temp=Double.toString(weatherResponse.getMain().getTemp());
                    String temp1=Integer.toString(weatherResponse.getMain().getHumidity());
                    String temp2=weatherResponse.getName();
                    String temp3=(weatherResponse.getWeather().get(0).getDescription());


                    Log.d(TAG, "Temperature: " + temp);
                    Log.d(TAG, "Humidity: " + temp1);
                    Log.d(TAG, "Country: " + temp2);
                    Log.d(TAG, "Conditions: " + temp3);


                    //TODO implement RecyclerView
                    // loadRecyclerView(response.body());
                } else {
                    Log.d(TAG, "onResponse: Failure");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: Failure" + t.getLocalizedMessage());

            }
        });
    }

    public void populateFragments()
    {
        tvTemp = findViewById(R.id.temperature);
        ivWeather = findViewById(R.id.weathericon);
        tvLocation = findViewById(R.id.location);
        tvConditions = findViewById(R.id.conditions);
        ivWeather = findViewById(R.id.weathericon);

        // TODO create weather Icon path string using icon info.

        Glide.with(this)
                .load("http://openweathermap.org/img/wn/09d@2x.png")
                .override(200, 200)
                .into(ivWeather);

        tvLocation.setText(weatherResponse.getName()+", "+weatherResponse.getSys().getCountry());
        int round_temp = (int)(weatherResponse.getMain().getTemp());
        tvTemp.setText(Integer.toString(round_temp));
        tvConditions.setText(weatherResponse.getWeather().get(0).getDescription());
    }
}

