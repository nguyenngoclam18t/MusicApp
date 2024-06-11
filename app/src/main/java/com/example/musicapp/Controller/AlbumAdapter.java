package com.example.musicapp.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<PlaylistModel> albumList;
    private OnAlbumClick onAlbumClick;

    public AlbumAdapter(List<PlaylistModel> albumList, OnAlbumClick onAlbumClick) {
        this.albumList = albumList;
        this.onAlbumClick = onAlbumClick;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_list_item, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        PlaylistModel album = albumList.get(position);
        holder.bind(album, onAlbumClick);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumThumbnail;
        private TextView albumTitle;
        private TextView albumDescription;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumThumbnail = itemView.findViewById(R.id.img_Album);
            albumTitle = itemView.findViewById(R.id.tvAlbumTitle);
            albumDescription = itemView.findViewById(R.id.albumDescription);
        }

        public void bind(PlaylistModel album, OnAlbumClick onAlbumClick) {
            albumTitle.setText(album.getPlaylistName());

            String description = album.getSortDescription();
            if (description != null) {
                albumDescription.setText(description);
            } else {
                albumDescription.setText("");
            }

            Picasso.get().load(album.getThumbnailLm()).into(albumThumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAlbumClick.OnAlbumClick(album);
                }
            });

        }

    }
}
