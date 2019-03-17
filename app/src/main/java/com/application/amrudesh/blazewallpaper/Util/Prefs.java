package com.application.amrudesh.blazewallpaper.Util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
        SharedPreferences sharedPreferences;


        public Prefs(Activity activity) {
            sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
        }

        public void setResolution(String s)
        {
            sharedPreferences.edit().putString("resolution",s).apply();
        }
        public String getResolution()
        {
            return sharedPreferences.getString("resolution","Tom");
        }
}
