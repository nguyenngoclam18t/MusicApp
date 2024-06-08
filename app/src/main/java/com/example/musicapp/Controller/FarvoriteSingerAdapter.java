package com.example.musicapp.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.OnArtistClick;
import com.example.musicapp.R;
import com.example.musicapp.View.ArtistProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FarvoriteSingerAdapter extends RecyclerView.Adapter<FarvoriteSingerAdapter.ViewHolder> {
    ArrayList<ArtistsModel> arr;
    private OnArtistClick listener;

    private Context context;


    public FarvoriteSingerAdapter(ArrayList<ArtistsModel> arr, OnArtistClick listener) {
        this.arr = arr;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FarvoriteSingerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favoritesinger,parent,false);

        return new ViewHolder(inflate, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FarvoriteSingerAdapter.ViewHolder holder, int position) {
        ArtistsModel artistsModel=arr.get(position);
        Picasso.get()
                .load(artistsModel.getThumbnailLm())
                .into(holder.img);
        holder.title.setText(artistsModel.getArtistName());
        holder.bind(artistsModel);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        OnArtistClick listener;
        ImageView img;
        TextView title;
        public ViewHolder(@NonNull View itemView, OnArtistClick listener) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFarvoriteSingerHP);
            title = itemView.findViewById(R.id.titleFarvoriteSingerHP);
            this.listener = listener;
        }
        public void bind(final ArtistsModel artist) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArtistClick(artist);

                    if (context instanceof FragmentActivity) {
                        Bundle bundle = new Bundle();
//                        bundle.putString("artistId", artist.getArtistId());
//                        bundle.putString("artistName", artist.getArtistName());
//                        bundle.putString("avatarUrl", artist.getAvatarUrl());

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
