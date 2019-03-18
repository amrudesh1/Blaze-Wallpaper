package com.application.amrudesh.blazewallpaper.Util;

import com.application.amrudesh.blazewallpaper.BuildConfig;

public class Constants {

    public static String LATEST_IMAGE_LINK = "https://api.unsplash.com/photos?per_page=50&client_id=" + BuildConfig.ApiKey;
    public static String IMAGE_DISPLAY_LINK="https://source.unsplash.com/";
    public static String TOP_RATED_IMAGE_LINK = "https://api.unsplash.com/photos?&per_page=50&order_by=popular&client_id="+BuildConfig.ApiKey;
}