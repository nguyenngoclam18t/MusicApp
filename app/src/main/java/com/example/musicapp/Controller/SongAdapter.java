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
import com.example.musicapp.R;
import com.example.musicapp.View.PlayerActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<SongModel> songList;

    public SongAdapter(List<SongModel> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongModel song = songList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvSongTitle, tvArtist;
        ImageView imgSong;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSongTitle = itemView.findViewById(R.id.tvSongTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            imgSong = itemView.findViewById(R.id.img_song);
            itemView.setOnClickListener(this);
        }

        public void bind(SongModel song) {
            tvSongTitle.setText(song.getTitle());
//            tvArtist.setText(song.getArtistId());
//            Picasso.get().load(song.getImgUrl()).into(imgSong);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                SongModel song = songList.get(position);
                Context context = itemView.getContext();
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("songId", song.getSongId());
                context.startActivity(intent);
            }
        }
    }
}
