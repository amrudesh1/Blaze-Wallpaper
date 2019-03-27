package com.application.amrudesh.blazewallpaper.Data;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallpaper_table")
public class Wallpaper implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String wallpaper_URL;
    private String wallpaper_URL_Thump;
    private String author_name;
    private Boolean fav_Btn;

    public Wallpaper(String id, String wallpaper_URL, String wallpaper_URL_Thump, String author_name, Boolean fav_Btn) {
        this.id = id;
        this.wallpaper_URL = wallpaper_URL;
        this.wallpaper_URL_Thump = wallpaper_URL_Thump;
        this.author_name = author_name;
        this.fav_Btn = fav_Btn;
    }

    @Ignore
    public Wallpaper() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWallpaper_URL() {
        return wallpaper_URL;
    }

    public void setWallpaper_URL(String wallpaper_URL) {
        this.wallpaper_URL = wallpaper_URL;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public Boolean getFav_Btn() {
        return fav_Btn;
    }

    public void setFav_Btn(Boolean fav_Btn) {
        this.fav_Btn = fav_Btn;
    }

    public String getWallpaper_URL_Thump() {
        return wallpaper_URL_Thump;
    }

    public void setWallpaper_URL_Thump(String wallpaper_URL_Thump) {
        this.wallpaper_URL_Thump = wallpaper_URL_Thump;
    }
}
