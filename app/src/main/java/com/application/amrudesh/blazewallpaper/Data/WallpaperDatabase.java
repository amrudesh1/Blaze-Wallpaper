package com.application.amrudesh.blazewallpaper.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Wallpaper.class}, version = 1)
public abstract class WallpaperDatabase extends RoomDatabase {
    private static WallpaperDatabase instance;

    public abstract WallpaperDao wallpaperDao();

    public static synchronized WallpaperDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WallpaperDatabase.class, "Wallpaper_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
