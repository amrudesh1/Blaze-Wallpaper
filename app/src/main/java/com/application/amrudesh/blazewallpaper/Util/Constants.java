package com.application.amrudesh.blazewallpaper.Util;

import com.application.amrudesh.blazewallpaper.BuildConfig;

public class Constants {

    public static String IMAGE_DISPLAY_LINK_LEFT = "https://api.unsplash.com/photos/";
    public static String IMAGE_DISPLAY_LINK_RIGHT="?client_id=" + BuildConfig.ApiKey;

    public static String Search_Link_Left = "https://api.unsplash.com/search/photos?per_page=30&page=";
    public static String Search_Link_right = "&client_id=" + BuildConfig.ApiKey;
    public static String Download_Link_Left = "https://api.unsplash.com/photos/";
    public static String Download_Link_Right = "/download"+IMAGE_DISPLAY_LINK_RIGHT;

    //Latest Image Link
    public static String LATEST_IMAGE_PAGINATION_NEW_LEFT = "https://api.unsplash.com/photos?per_page=30&page=";
    public static String LATEST_IMAGE_PAGINATION_NEW_RIGHT = "&client_id=" + BuildConfig.ApiKey;

    //Top Rated Image Link
    public static String TOP_RATED_IMAGE_LINK_LEFT = "https://api.unsplash.com/photos?per_page=30&page=";
    public static String TOP_RATED_IMAGE_LINK_RIGHT ="&order_by=popular&client_id="+BuildConfig.ApiKey;

}
