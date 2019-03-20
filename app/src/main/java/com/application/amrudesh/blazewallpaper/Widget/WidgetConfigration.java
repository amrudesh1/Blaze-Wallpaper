package com.application.amrudesh.blazewallpaper.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.application.amrudesh.blazewallpaper.Data.Wallpaper;
import com.application.amrudesh.blazewallpaper.Data.WallpaperViewModel;
import com.application.amrudesh.blazewallpaper.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;


public class WidgetConfigration extends AppCompatActivity {
    WallpaperViewModel wallpaperViewModel;
    @BindView(R.id.add_button)
    Button btn;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    View.OnClickListener AddToHomeScreen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wallpaperViewModel.getAllWallpapers().observe(WidgetConfigration.this, new Observer<List<Wallpaper>>() {
                @Override
                public void onChanged(List<Wallpaper> wallpapers) {
                    WidgetRemoteService.setWallpaperArrayList(wallpapers);
                }
            });
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetConfigration.this);
            WallpaperWidget.updateAppWidget(WidgetConfigration.this, appWidgetManager, appWidgetId);
            RemoteViews remoteViews= new RemoteViews(getPackageName(),R.layout.wallpaper_widget);
            Intent intent = new Intent(WidgetConfigration.this, WidgetRemoteService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(R.id.listViewWidget,intent);
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
            setResult(RESULT_OK, new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId));
            finish();

        }
    };


    public WidgetConfigration() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_configration);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
         if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED,resultValue);
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        btn.setOnClickListener(AddToHomeScreen);
        wallpaperViewModel = ViewModelProviders.of(this).get(WallpaperViewModel.class);
    }
}
