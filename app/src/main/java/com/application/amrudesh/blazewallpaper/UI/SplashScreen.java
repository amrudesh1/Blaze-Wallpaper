package com.application.amrudesh.blazewallpaper.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.amrudesh.blazewallpaper.R;
import com.application.amrudesh.blazewallpaper.Util.Prefs;
import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {
    RelativeLayout Layout1, Layout2;
    Handler handler;
    @BindView(R.id.getStarted)
    MaterialButton startButton;
    @BindView(R.id.device_name)
    TextView deviceName;
    @BindView(R.id.resolution)
    TextView resoltion;
    private int width;
    private int height;
    private Boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        getDisplayMetrics();
        getSupportActionBar().hide();
        Layout1 = (RelativeLayout) findViewById(R.id.rellay1);
        Layout2 = (RelativeLayout) findViewById(R.id.rellay2);
        Prefs prefs = new Prefs(this);
        firstTime = prefs.getState();
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Layout1.setVisibility(View.VISIBLE);
                Layout2.setVisibility(View.VISIBLE);
                deviceName.setText(Build.DEVICE);
                resoltion.setText(width + " X " + height);


            }
        };
        if (firstTime) {
            startActivity(new Intent(SplashScreen.this, Navigation.class));
            finish();
        }

        Log.i("SPLASH", String.valueOf(width + "X" + height));
        handler.postDelayed(runnable, 2000);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstTime = true;
                prefs.setResolution(width, height, firstTime);
                startActivity(new Intent(SplashScreen.this, Navigation.class));
                finish();
            }
        });
    }


    private void getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

    }
}
