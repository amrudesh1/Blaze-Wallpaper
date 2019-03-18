package com.application.amrudesh.blazewallpaper.Data;

import java.io.Serializable;

public class Wallpaper implements Serializable {
    private String id;
    private String wallpaper_URL;
    private String author_name;
    private Boolean fav_Btn;

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
}
