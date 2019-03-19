package com.application.amrudesh.blazewallpaper.Data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class WallpaperRepository {
    private WallpaperDao wallpaperDao;
    private LiveData<List<Wallpaper>> Allwallpapers;

    public WallpaperRepository(Application application) {
        WallpaperDatabase database = WallpaperDatabase.getInstance(application);
        wallpaperDao = database.wallpaperDao();
        Allwallpapers = wallpaperDao.getAllWallpapers();
    }

    public void insert(Wallpaper wallpaper) {
        new insertWallpaper(wallpaperDao).execute(wallpaper);
    }

    public void delete(Wallpaper wallpaper) {
        new deleteWallpaper(wallpaperDao).execute(wallpaper);
    }

    public LiveData<List<Wallpaper>> getAllwallpapers() {
        return Allwallpapers;
    }

    private static class insertWallpaper extends AsyncTask<Wallpaper, Void, Void> {
        private WallpaperDao wallpaperDao;

        private insertWallpaper(WallpaperDao wallpaperDao) {
            this.wallpaperDao = wallpaperDao;
        }

        @Override
        protected Void doInBackground(Wallpaper... wallpapers) {
            wallpaperDao.insert(wallpapers[0]);
            return null;
        }
    }

    private static class deleteWallpaper extends AsyncTask<Wallpaper, Void, Void> {
        private WallpaperDao wallpaperDao;

        private deleteWallpaper(WallpaperDao wallpaperDao) {
            this.wallpaperDao = wallpaperDao;
        }

        @Override
        protected Void doInBackground(Wallpaper... wallpapers) {
            wallpaperDao.delete(wallpapers[0]);
            return null;
        }
    }
}
