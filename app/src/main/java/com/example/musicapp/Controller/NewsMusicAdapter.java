package com.example.musicapp.Controller;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.example.musicapp.View.HomePageFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;

public class NewsMusicAdapter extends RecyclerView.Adapter<NewsMusicAdapter.ViewHolder> {
    ArrayList<SongModel> arr;
    private OnSongClick songClick;

    public NewsMusicAdapter(ArrayList<SongModel> arr) {
        this.arr = arr;
    }
    @NonNull
    @Override
    public NewsMusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_card_recyclevie_homepage,parent,false);
        return new ViewHolder(inflate);
    }

    public NewsMusicAdapter(ArrayList<SongModel> arr, OnSongClick songClick) {
        this.arr = arr;
        this.songClick = songClick;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsMusicAdapter.ViewHolder holder, int position) {
        SongModel song = arr.get(position);
        Picasso.get()
                .load(arr.get(position).getImgUrl())
                .into(holder.img);
        holder.title.setText(arr.get(position).getTitle());
        holder.singer.setText(arr.get(position).getArtistId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songClick.onSongClick(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,singer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.imgCardHomePage);
            title=(TextView) itemView.findViewById(R.id.titleCardHomePage);
            singer=(TextView)itemView.findViewById(R.id.descCardHomePage);

        }
    }
}