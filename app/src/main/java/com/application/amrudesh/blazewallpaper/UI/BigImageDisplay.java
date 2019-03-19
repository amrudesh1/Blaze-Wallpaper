package com.application.amrudesh.blazewallpaper.UI;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.application.amrudesh.blazewallpaper.Data.Wallpaper;
import com.application.amrudesh.blazewallpaper.Data.WallpaperViewModel;
import com.application.amrudesh.blazewallpaper.R;
import com.application.amrudesh.blazewallpaper.Util.Constants;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
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
    LottieAnimationView animationView;
    Boolean isPressed;
    WallpaperViewModel wallpaperViewModel;
    Wallpaper wallpaper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_display);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        wallpaper =(Wallpaper)getIntent().getSerializableExtra("URL");
        url = wallpaper.getId();
        Log.i("TAG1",String.valueOf(wallpaper.getFav_Btn()));
        animationView = findViewById(R.id.animation_view);
        isPressed = wallpaper.getFav_Btn();
        wallpaperViewModel = ViewModelProviders.of(this).get(WallpaperViewModel.class);
        checkPermission();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Picasso.get().
                load(Constants.IMAGE_DISPLAY_LINK + url + "/1080x1920").
                into(bigImageView);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDownload();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallaper();
            }
        });
        btnStatus();
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPressed) {
                    animationView.playAnimation();
                    isPressed = true;
                    Log.i("tag",String.valueOf(isPressed));
                    addWallpapertofav(wallpaper.getId(),wallpaper.getWallpaper_URL(),wallpaper.getAuthor_name(),isPressed);
                    Toast.makeText(BigImageDisplay.this, "Image Added To Favourites", Toast.LENGTH_LONG).show();
                } else {
                    animationView.setProgress(0);
                    isPressed = false;
                    deleteWallpaper();
                    Toast.makeText(BigImageDisplay.this, "Image Removed To Favourites", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }

            }


        }
    }

    private void StartDownload() {
        File direct = new File(Environment.DIRECTORY_PICTURES
                + "/Blaze_Wallpapers");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        String download_url = Constants.Download_Link_Left + url + Constants.Download_Link_Right;
        Log.i("TAG", download_url);
        DownloadManager.Request request =
                new DownloadManager.Request(Uri.parse(download_url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(url);
        request.setDescription("Download File");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES + "/Blaze_Wallpapers", url + ".jpg");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


    }

    private void setWallaper() {
        File direct = new File(Environment.DIRECTORY_PICTURES
                + "/Blaze_Wallpapers");
        File[] dirFiles = direct.listFiles();
        if (dirFiles.length != 0) {
            // loops through the array of files, outputing the name to console
            for (int ii = 0; ii < dirFiles.length; ii++) {
                String fileOutput = dirFiles[ii].toString();
                Log.i("OUTPUT", fileOutput);
            }
        }
    }

    public void addWallpapertofav(String id, String url, String auth_name, Boolean btn) {
        Wallpaper wallpaper = new Wallpaper(id,url,auth_name,btn);
        wallpaperViewModel.insert(wallpaper);
    }
    public void deleteWallpaper()
    {
        wallpaperViewModel.delete(wallpaper);
    }

    public  void btnStatus()
    {
        if (isPressed)
        {
            animationView.playAnimation();
            animationView.setProgress(1.0f);
        }
        else
        {
            animationView.setProgress(0f);
        }
    }
}