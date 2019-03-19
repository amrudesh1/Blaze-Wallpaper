package com.application.amrudesh.blazewallpaper.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.application.amrudesh.blazewallpaper.R;
import com.application.amrudesh.blazewallpaper.Util.Constants;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
        Log.i("URL", url);
        checkPermission();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Picasso.get().
                load(Constants.IMAGE_DISPLAY_LINK + url + "/1080x1920").
                into(bigImageView);


    }

    private void  checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);

            } else {
                ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }

        }


    }

}
