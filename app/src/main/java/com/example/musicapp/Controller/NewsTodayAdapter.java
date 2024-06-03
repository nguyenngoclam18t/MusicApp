package com.example.musicapp.Controller;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.AlbumModel;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.OnArtistClick;
import com.example.musicapp.R;
import com.example.musicapp.View.ArtistProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsTodayAdapter extends RecyclerView.Adapter<NewsTodayAdapter.ViewHolder> {

    ArrayList<ArtistsModel> lst;
    private OnArtistClick listener;

    private Context context;

    public NewsTodayAdapter(ArrayList<ArtistsModel> lst, OnArtistClick listener) {
        this.lst = lst;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsTodayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_newstoday,parent,false);

        return new ViewHolder(inflate, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlbumModel albumModel=lst.get(position);

        Picasso.get()
                .load(lst.get(position).getAvatarUrl())
                .placeholder(R.drawable.loading)
                .into(holder.img);
        holder.title.setText(lst.get(position).getArtistName());
        ArtistsModel artistsModel = lst.get(position);
        holder.title.setText(albumModel.albumId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "đây là click "+albumModel.albumId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OnArtistClick listener;
        ImageView img;
        TextView title;
        public ViewHolder(@NonNull View itemView, OnArtistClick listener) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.titleNewsTodayHomePage);
            img=(ImageView) itemView.findViewById(R.id.imgNewsTodayHomePage);
            this.listener = listener;
        }

        public void bind(final ArtistsModel artist) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArtistClick(artist);

                    if (context instanceof FragmentActivity) {
                        Bundle bundle = new Bundle();
                        bundle.putString("artistId", artist.getArtistId());
                        bundle.putString("artistName", artist.getArtistName());
                        bundle.putString("avatarUrl", artist.getAvatarUrl());

                        ArtistProfileFragment artistProfileFragment = new ArtistProfileFragment();
                        artistProfileFragment.setArguments(bundle);

                        ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.FrameHomePage, artistProfileFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }
    }
}
