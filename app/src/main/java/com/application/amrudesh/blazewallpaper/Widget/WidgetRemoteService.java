package com.application.amrudesh.blazewallpaper.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.application.amrudesh.blazewallpaper.Data.Wallpaper;
import com.application.amrudesh.blazewallpaper.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WidgetRemoteService extends RemoteViewsService {

    private static List<Wallpaper> wallpaperArrayList = new ArrayList<>();

    public static void setWallpaperArrayList(List<Wallpaper> data) {
        WidgetRemoteService.wallpaperArrayList = data;
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(WidgetRemoteService.this, intent));

    }

    class ListProvider implements RemoteViewsFactory {

        Context mContext;
        int appWidgetId;


        public ListProvider(Context mContext, Intent intent) {
            this.mContext = mContext;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 1);
        }


        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            Log.i("LISt", String.valueOf(wallpaperArrayList.size()));
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return wallpaperArrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            Log.i("TAG", "REMOTEVIEW INVOKED");
            try {
                Bitmap b = Picasso.get().load(wallpaperArrayList.get(position).getWallpaper_URL()).get();
                views.setImageViewBitmap(R.id.widgetImageView, b);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
