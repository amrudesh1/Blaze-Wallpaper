package com.application.amrudesh.blazewallpaper.UI;

import android.os.Bundle;

import com.application.amrudesh.blazewallpaper.Data.Wallpaper;
import com.application.amrudesh.blazewallpaper.Data.WallpaperViewModel;
import com.application.amrudesh.blazewallpaper.Model.NewImageAdapter;
import com.application.amrudesh.blazewallpaper.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Favourites extends AppCompatActivity {



    private List<Wallpaper> wallpaperList;
    private NewImageAdapter imageAdapter;
    WallpaperViewModel wallpaperViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        setTitle("Favourite Wallpapers");
        RecyclerView recyclerView = findViewById(R.id.fav_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        wallpaperList = new ArrayList<>();
        imageAdapter = new NewImageAdapter(this,wallpaperList);
        recyclerView.setAdapter(imageAdapter);
        setUpViewModel();


    }
    private void setUpViewModel()
    {
        wallpaperViewModel = ViewModelProviders.of(this).get(WallpaperViewModel.class);
        wallpaperViewModel.getAllWallpapers().observe(this, new Observer<List<Wallpaper>>() {
            @Override
            public void onChanged(List<Wallpaper> wallpapers) {
                imageAdapter.setWallpapers(wallpapers);
            }
        });
    }
}
