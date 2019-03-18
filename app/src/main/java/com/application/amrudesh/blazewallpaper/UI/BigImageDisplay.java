package com.application.amrudesh.blazewallpaper.UI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.application.amrudesh.blazewallpaper.R;
import com.application.amrudesh.blazewallpaper.Util.Constants;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BigImageDisplay extends AppCompatActivity {
    String url;
    @BindView(R.id.bigImageView)
    ImageView bigImageView;
    @BindView(R.id.download_btn)
    MaterialButton downloadBtn;
    @BindView(R.id.save_btn)
    MaterialButton saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_display);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        url = getIntent().getStringExtra("URL");
        Log.i("URL",url);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Picasso.get().
                load(Constants.IMAGE_DISPLAY_LINK + url + "/1080x1920").
                into(bigImageView);


    }

}
