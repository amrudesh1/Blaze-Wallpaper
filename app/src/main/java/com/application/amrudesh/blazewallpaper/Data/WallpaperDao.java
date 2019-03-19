package com.application.amrudesh.blazewallpaper.Data;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface WallpaperDao {

    @Insert
    void insert(Wallpaper wallpaper);

    @Delete
    void delete(Wallpaper wallpaper);

    @Query("SELECT * FROM wallpaper_table ORDER BY id")
    LiveData<List<Wallpaper>> getAllWallpapers();

}
