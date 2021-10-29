package com.example.hdwallpaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Myclass>{


    Wallpaper_Activity wallpaper_activity;
    ArrayList<WallpaperModel> imglist;
    ArrayList<String> finalImg;



    public RecyclerViewAdapter(Wallpaper_Activity wallpaper_activity, ArrayList imglist) { // constructpr for array and activity
        this.wallpaper_activity = wallpaper_activity;
        this.imglist = imglist;
    }

    @NonNull
    @Override
    public Myclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {    // layout inflator
        View view = LayoutInflater.from(wallpaper_activity).inflate(R.layout.image_layout,parent,false);
        return new Myclass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myclass holder, int position) {   // view binder
        Glide.with(wallpaper_activity).load(imglist.get(position).getMediulurl()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pagerIntent = new Intent(wallpaper_activity, FullScreenActivity2.class);
                pagerIntent.putExtra("pos",position);
                pagerIntent.putExtra("imagelist", imglist.get(position).getOriginalUrl());

                wallpaper_activity.startActivity(pagerIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return imglist.size();
    }


    class Myclass extends RecyclerView.ViewHolder {   // custom view holder class
        ImageView imageView;
        public Myclass(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
