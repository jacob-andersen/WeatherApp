package com.example.weatherapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://api.openweathermap.org/";
    private static Retrofit retrofit;


  private RetrofitClient(){}

  public static Retrofit getRetrofit () {

      if (retrofit == null) {
          retrofit = new Retrofit.Builder()
                  .baseUrl(BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
      }
      return retrofit;
  }
}
