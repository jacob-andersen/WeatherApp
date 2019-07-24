package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.weatherapp.CurrentWeatherModel.WeatherResponse;
import com.example.weatherapp.WeatherForecastModel.WeatherForecastResponse;
import com.example.weatherapp.retrofit.RetrofitClient;
import com.example.weatherapp.retrofit.WeatherService;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Weather";

    TextView tvCurrentTime;
    TextView tvLocation;
    TextView tvTemp;
    TextView tvConditions;
    ImageView ivWeather;
    TextView tvWindspeed;
    TextView tvHumidity;
    TextView tvForecastTime;

    public WeatherResponse weatherResponse;
    public WeatherForecastResponse weatherForecastResponse;
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
        fetch5DayForecast();

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
//                    String temp4=(weatherResponse.getWeather().get(0).getIcon());


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

    public void fetch5DayForecast() {

        // 1: Declare Service and Initialize it using RetrofitClient
        WeatherService weatherService =
                RetrofitClient
                        .getRetrofit()
                        .create(WeatherService.class);

        // 2: Declare Service Return type and initialize it using the Service
        final Call<WeatherForecastResponse> weatherForecastResponseCall = weatherService.getForecast(88319, units,appid);

        // 3: Use the Call from Step 2 and call the .enqueue method
        weatherForecastResponseCall.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, retrofit2.Response<WeatherForecastResponse> response) {

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: Success");
                    weatherForecastResponse = response.body();
                    populateFragments();

                    String temp=Double.toString(weatherForecastResponse.getList().get(10).getWind().getSpeed());
                    String temp1=Double.toString(weatherForecastResponse.getList().get(0).getMain().getTemp());

                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm a");
                    Date conv_date = new Date((weatherForecastResponse.getList().get(0).getDt())*1000L);
                    String date = sdf.format(conv_date);

                    Log.d(TAG, "In Weather: Forecast Wind Speed: " + temp);
                    Log.d(TAG, "In Weather: Forecast Temperature: " + temp1);
                    Log.d(TAG, "In Weather: UNIX Time " + conv_date);


                    //TODO implement RecyclerView
                    // loadRecyclerView(response.body());
                } else {
                    Log.d(TAG, "onResponse: Failure");
                }
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: Failure" + t.getLocalizedMessage());

            }
        });
    }

    public void populateFragments()
    {   tvCurrentTime = findViewById(R.id.current_time);
        tvTemp = findViewById(R.id.temperature);
        ivWeather = findViewById(R.id.weathericon);
        tvLocation = findViewById(R.id.location);
        tvConditions = findViewById(R.id.conditions);
        ivWeather = findViewById(R.id.weathericon);
        tvWindspeed = findViewById(R.id.windspeed);
        tvHumidity = findViewById(R.id.humidity);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm a");
        Date conv_date = new Date(weatherResponse.getDt()*1000L);
        String date = sdf.format(conv_date);

        // build ressource path to Weather Icon, using icon info from json
        String iconPath = "http://openweathermap.org/img/wn/"+weatherResponse.getWeather().get(0).getIcon()+"@2x.png";

        Glide.with(this)
                .load(iconPath)
                .override(300, 200)
                .into(ivWeather);

        tvLocation.setText(weatherResponse.getName()+", "+weatherResponse.getSys().getCountry());
        tvCurrentTime.setText(date);
        tvTemp.setText(Integer.toString((int)weatherResponse.getMain().getTemp()) + " F");
        tvConditions.setText(weatherResponse.getWeather().get(0).getDescription());
        tvWindspeed.setText("wind speed "+Double.toString((int)weatherResponse.getWind().getSpeed())+ " Mph");
        tvHumidity.setText("humidity " + Integer.toString(weatherResponse.getMain().getHumidity()) + "%");
    }
}

