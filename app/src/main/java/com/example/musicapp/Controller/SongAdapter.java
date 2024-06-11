package com.example.musicapp.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.SongModel;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.R;
import com.example.musicapp.View.PlayerActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<SongModel> songList;
    private Context context;
    private OnSongClick onSongClick;

    public SongAdapter(ArrayList<SongModel> songList, Context context, OnSongClick onSongClick) {
        this.songList = songList;
        this.context = context;
        this.onSongClick = onSongClick;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_list_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongModel song = songList.get(position);
        holder.songTitle.setText(song.getTitle());
        holder.artistName.setText(song.getArtistsNames());
        if (song.getThumbnailLm() != null && !song.getThumbnailLm().isEmpty()) {
            Picasso.get().load(song.getThumbnailLm()).into(holder.songImage);
        } else {
            Picasso.get().load(R.drawable.img).into(holder.songImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSongClick.onSongClick(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void setOnClickListener(OnSongClick song) {
        this.onSongClick = song;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView songImage;
        TextView songTitle, artistName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.img_song);
            songTitle = itemView.findViewById(R.id.tvSongTitle);
            artistName = itemView.findViewById(R.id.tvArtist);
        }
    }
}
