package com.example.musicapp.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.AlbumModel;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<AlbumModel> albumList;
    private OnAlbumClick albumClick;
    public AlbumAdapter(List<AlbumModel> albumList) {
        this.albumList = albumList;
    }

    public AlbumAdapter(ArrayList<AlbumModel> albumList, OnAlbumClick albumClick) {

        this.albumList = albumList;
        this.albumClick=albumClick;
    }
    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_list_item, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        AlbumModel album = albumList.get(position);
        holder.bind(album);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumClick.OnAlbumClick(album);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView albumTitle, artistName;
        ImageView imgAlbum;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.tvAlbumTitle);
            imgAlbum = itemView.findViewById(R.id.img_Album);
            itemView.setOnClickListener(this);
        }

        public void bind(AlbumModel album) {
            albumTitle.setText(album.getAlbumName());
            Picasso.get().load(album.getImageUrl()).into(imgAlbum);
        }

        @Override
        public void onClick(View v) {
            int position = getAbsoluteAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                AlbumModel album = albumList.get(position);
                Context context = itemView.getContext();
            }
        }
    }
}
