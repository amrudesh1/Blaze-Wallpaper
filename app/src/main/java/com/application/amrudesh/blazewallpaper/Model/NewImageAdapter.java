package com.application.amrudesh.blazewallpaper.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.application.amrudesh.blazewallpaper.Data.Wallpaper;
import com.application.amrudesh.blazewallpaper.R;
import com.application.amrudesh.blazewallpaper.UI.BigImageDisplay;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewImageAdapter extends RecyclerView.Adapter<NewImageAdapter.ViewHolder> {
    Context context;
    List<Wallpaper> wallpaperList;
    Wallpaper wallpaper;
    String id;

    public NewImageAdapter(Context context, List<Wallpaper> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;

    }

    @NonNull
    @Override
    public NewImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout,parent,false);
        return new NewImageAdapter.ViewHolder(context,view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewImageAdapter.ViewHolder holder, int position) {
     wallpaper = wallpaperList.get(position);
        Picasso.get()
                .load(wallpaper.getWallpaper_URL())
                .into(holder.mainImage);


    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.main_image)
        ImageView mainImage;
        private ViewHolder(Context ctx, View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context = ctx;
            mainImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ctx, BigImageDisplay.class);
                    i.putExtra("URL",wallpaperList.get(getAdapterPosition()).getId());
                    ctx.startActivity(i);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
