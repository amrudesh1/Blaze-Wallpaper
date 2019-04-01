package com.application.amrudesh.blazewallpaper.UI;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.application.amrudesh.blazewallpaper.BuildConfig;
import com.application.amrudesh.blazewallpaper.Data.Wallpaper;
import com.application.amrudesh.blazewallpaper.Data.WallpaperViewModel;
import com.application.amrudesh.blazewallpaper.R;
import com.application.amrudesh.blazewallpaper.Util.Constants;
import com.application.amrudesh.blazewallpaper.Util.Prefs;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AlertDialog;
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
    LottieAnimationView animationView, animationView2, animationView3;
    Boolean isPressed;
    WallpaperViewModel wallpaperViewModel;
    Wallpaper wallpaper;
    Target target;
    Bitmap bm;
    RequestQueue requestQueue;
    Prefs prefs;
    @BindView(R.id.adView_big)
    AdView adViewBig;
    @BindView(R.id.home_screen_btn)
    ImageView home;
    @BindView(R.id.lock_screen_btn)
    ImageView lock;
    Boolean visibility = false;
    static WallpaperManager wallpaperManager;
    static ProgressBar progressBar;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x1 = 0, x2, y1, y2;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                if (x1 < x2) {
                    showDialog();
                    prefs.setSwipeStatus(true);
                    animationView3.setVisibility(View.GONE);
                    animationView3.cancelAnimation();
                    Toast.makeText(this, "Right Swipe", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_display);
        ButterKnife.bind(this);
        prefs = new Prefs(this);
        getSupportActionBar().hide();
        requestQueue = Volley.newRequestQueue(this);
        wallpaper = (Wallpaper) getIntent().getSerializableExtra("URL");
        url = wallpaper.getId();
        Log.i("WALLPAPER", wallpaper.getWallpaper_URL());
        progressBar = findViewById(R.id.progress_circular);
        animationView = findViewById(R.id.animation_view);
        animationView2 = findViewById(R.id.animation_view_loading);
        animationView3 = findViewById(R.id.swipe_right_animation);
        isPressed = wallpaper.getFav_Btn();
        wallpaperViewModel = ViewModelProviders.of(this).get(WallpaperViewModel.class);
        checkPermission();
        wallpaperManager = WallpaperManager.getInstance(BigImageDisplay.this);
        progressBar.setVisibility(View.GONE);
        progressBar.setMax(100);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (!BuildConfig.IS_PAID) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adViewBig.loadAd(adRequest);
        } else {
            adViewBig.setVisibility(View.GONE);
            adViewBig.destroy();
        }
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                bigImageView.setImageBitmap(bitmap);
                Bitmap.Config config;
                bm = bitmap;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get()
                .load(wallpaper.getWallpaper_URL())
                .into(target);

        animationView2.cancelAnimation();
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWallpaperList(url);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallaper(visibility);
            }
        });
        btnStatus();
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPressed) {
                    animationView.playAnimation();
                    isPressed = true;
                    Log.i("tag", String.valueOf(isPressed));
                    addWallpapertofav(wallpaper.getId(), wallpaper.getWallpaper_URL(), wallpaper.getPortFolio_url(), wallpaper.getWallpaper_URL_Thump(), wallpaper.getAuthor_name(), isPressed);
                    Toast.makeText(BigImageDisplay.this, "Image Added To Favourites", Toast.LENGTH_LONG).show();
                } else {
                    animationView.setProgress(0);
                    isPressed = false;
                    deleteWallpaper();
                    Toast.makeText(BigImageDisplay.this, "Image Removed To Favourites", Toast.LENGTH_LONG).show();
                }
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myTask();
            }
        }, 15000);

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new setLockScreen().execute(bm);
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

    private void StartDownload(String download_url) {
        File direct = new File(Environment.DIRECTORY_PICTURES
                + "/Blaze_Wallpapers");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        Log.i("TAG", download_url);
        DownloadManager.Request request =
                new DownloadManager.Request(Uri.parse(download_url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(url);
        request.setDescription("Downloading Image");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES + "/Blaze_Wallpapers", url + ".jpg");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


    }

    private void setWallaper(Boolean visibile) {
        downloadBtn.setVisibility(View.GONE);
        saveBtn.setVisibility(View.GONE);
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!visibile) {
                            downloadBtn.setVisibility(View.GONE);
                            saveBtn.setVisibility(View.GONE);
                            home.setVisibility(View.VISIBLE);
                            lock.setVisibility(View.VISIBLE);
                            visibility = true;
                        } else {
                            downloadBtn.setVisibility(View.VISIBLE);
                            saveBtn.setVisibility(View.VISIBLE);
                            home.setVisibility(View.GONE);
                            lock.setVisibility(View.GONE);
                            visibility = false;
                        }
                    }
                }
                , 500);


    }


    public void addWallpapertofav(String id, String url, String port_link, String thump, String auth_name, Boolean btn) {
        Wallpaper wallpaper = new Wallpaper(id, url, port_link, thump, auth_name, btn);
        wallpaperViewModel.insert(wallpaper);
    }

    public void deleteWallpaper() {
        wallpaperViewModel.delete(wallpaper);
    }

    public void btnStatus() {
        if (isPressed) {
            animationView.playAnimation();
            animationView.setProgress(1.0f);
        } else {
            animationView.setProgress(0f);
        }
    }

    private void getWallpaperList(String id) {

        JsonObjectRequest wallpaperRequest = new JsonObjectRequest
                (Request.Method.GET,
                        Constants.Download_Link_Left + id + Constants.Download_Link_Right, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            StartDownload(response.getString("url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(wallpaperRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setWallaper(visibility);
    }


    private void showDialog() {
        TextView auth_name;
        TextView portfolio_link;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.infolayout, null);
        auth_name = view.findViewById(R.id.author_name);
        portfolio_link = view.findViewById(R.id.portfolio);
        auth_name.setText("Author Name:" + " " + wallpaper.getAuthor_name());
        portfolio_link.setText("Portfolio Link:" + " " + wallpaper.getPortFolio_url());
        alertDialogBuilder.setView(view);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void myTask() {
        if (prefs.getSwipeStatus()) {
            animationView3.setVisibility(View.GONE);
            animationView3.cancelAnimation();
            Log.i("TASK", "EXECUTED if");
        } else {
            animationView3.setVisibility(View.VISIBLE);
            Log.i("TASK", "EXECUTED ELSE");
            animationView3.playAnimation();
        }

    }

    private void setLockScreenWallpaper() {


    }

    private static class setLockScreen extends AsyncTask<Bitmap, Integer, Void> {
        int count = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            Log.i("COMPLETED", String.valueOf(values[0]));
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Log.i("BITMAP", String.valueOf(bitmaps[0].getByteCount()));
            bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    Log.i("WALLPAPER", String.valueOf(wallpaperManager.getDesiredMinimumHeight()
                            + ":" + wallpaperManager.getDesiredMinimumWidth()));
                    /*   wallpaperManager.setBitmap(bitmaps[0], null, true, WallpaperManager.FLAG_LOCK);*/
                    wallpaperManager.setStream(bs, null, true, WallpaperManager.FLAG_LOCK);
                    while (count < 5) {
                        SystemClock.sleep(1000);
                        count++;
                        publishProgress(count * 20);
                    }


                } catch (IOException e) {
                    Log.i("COMPLETED", e.toString());
                }


            }

            return null;
        }
    }
}

