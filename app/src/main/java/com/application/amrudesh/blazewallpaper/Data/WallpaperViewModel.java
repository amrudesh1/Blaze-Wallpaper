package com.application.amrudesh.blazewallpaper.Data;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class WallpaperViewModel extends AndroidViewModel {
    private WallpaperRepository wallpaperRepository;
    private LiveData<List<Wallpaper>> allWallpapers;

    public WallpaperViewModel(@NonNull Application application) {
        super(application);
        wallpaperRepository = new WallpaperRepository(application);
        allWallpapers = wallpaperRepository.getAllwallpapers();
    }

    public void insert(Wallpaper wallpaper) {
        wallpaperRepository.insert(wallpaper);
    }

    public void delete(Wallpaper wallpaper) {
        wallpaperRepository.delete(wallpaper);
    }

    public LiveData<List<Wallpaper>> getAllWallpapers() {
        return allWallpapers;
    }
}
