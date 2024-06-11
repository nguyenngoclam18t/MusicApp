package com.example.musicapp.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsMusicAdapter extends RecyclerView.Adapter<NewsMusicAdapter.ViewHolder> {
    private ArrayList<SongModel> arr;
    private OnSongClick songClick;

    public NewsMusicAdapter(ArrayList<SongModel> arr, OnSongClick songClick) {
        this.arr = arr;
        this.songClick = songClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_card_recyclevie_homepage, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongModel song = arr.get(position);

        if (song.getThumbnailLm() != null && !song.getThumbnailLm().isEmpty()) {
            Picasso.get()
                    .load(song.getThumbnailLm())
                    .placeholder(R.drawable.img)
                    .error(R.drawable.icon_music_note)
                    .into(holder.img, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            // Log the error or do something else
                            e.printStackTrace();
                        }
                    });
        } else {
            holder.img.setImageResource(R.drawable.img);
        }

        holder.title.setText(song.getTitle());
        holder.singer.setText(song.getArtistsNames());
        //holder.release.setText(song.getReleaseDate());

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, singer, release;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCardHomePage);
            title = itemView.findViewById(R.id.titleCardHomePage);
            singer = itemView.findViewById(R.id.descCardHomePage);
            release = itemView.findViewById(R.id.DateCardHomePage);
        }
    }
}
