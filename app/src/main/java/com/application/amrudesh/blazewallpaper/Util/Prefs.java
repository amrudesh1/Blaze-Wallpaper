package com.application.amrudesh.blazewallpaper.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences sharedPreferences;


    public Prefs(Activity activity) {
        sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void setResolution(int w, int h, Boolean b) {
        sharedPreferences.edit().putInt("width", w).apply();
        sharedPreferences.edit().putInt("height", h).apply();
        sharedPreferences.edit().putBoolean("state", b).apply();

    }

    public void setSwipeStatus(Boolean status)
    {
        sharedPreferences.edit().putBoolean("swipe",status).apply();
    }
    public Boolean getSwipeStatus()
    {
        return sharedPreferences.getBoolean("swipe",false);
    }
    public int getWidth() {
        return sharedPreferences.getInt("width", 1920);
    }

    public int getHeight() {
        return sharedPreferences.getInt("height", 1080);
    }

    public Boolean getState() {
        return sharedPreferences.getBoolean("state", false);
    }
}
