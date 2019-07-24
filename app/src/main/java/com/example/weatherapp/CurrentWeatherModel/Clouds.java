
package com.example.weatherapp.CurrentWeatherModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Clouds implements Serializable
{

    @SerializedName("all")
    @Expose
    private int all;
    private final static long serialVersionUID = -4862728334877231583L;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

}
