package com.example.musicapp.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumHomePageAdapter extends RecyclerView.Adapter<AlbumHomePageAdapter.ViewHolder> {

    private final ArrayList<PlaylistModel> lst;
    private OnAlbumClick albumClick;
    public AlbumHomePageAdapter(ArrayList<PlaylistModel> lst) {
        this.lst = lst;
    }
    public AlbumHomePageAdapter(ArrayList<PlaylistModel> lst, OnAlbumClick albumClick) {

        this.lst = lst;
        this.albumClick=albumClick;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_newstoday, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistModel playlistModel =lst.get(position);

        Picasso.get()
                .load(playlistModel.getThumbnailLm())
                .placeholder(R.drawable.loading)
                .into(holder.img);
        if(playlistModel.getSortDescription()==null||playlistModel.getSortDescription().isEmpty()){
            holder.title.setText(playlistModel.getPlaylistName());
        }else {
            holder.title.setText(playlistModel.getSortDescription());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumClick.OnAlbumClick(playlistModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleNewsTodayHomePage);
            img = itemView.findViewById(R.id.imgNewsTodayHomePage);
        }
    }
}