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
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class TopMusicHomePageAdapter extends RecyclerView.Adapter<TopMusicHomePageAdapter.ViewHolder> {
    ArrayList<SongModel> arr;

    public TopMusicHomePageAdapter(ArrayList<SongModel> arr) {
        this.arr = arr;
    }

    @NonNull
    @Override
    public TopMusicHomePageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_topmusic_homepage,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TopMusicHomePageAdapter.ViewHolder holder, int position) {
        Picasso.get()
                .load(arr.get(position).imgUrl)
                .into(holder.img);
        holder.title.setText(arr.get(position).Title);
        holder.desc.setText(arr.get(position).artistId);
        holder.stt.setText(Integer.toString(position+1));
    }
    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView stt,title,desc;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.imgTopHomePage);
            title=(TextView) itemView.findViewById(R.id.titleTopHomePage);
            desc=(TextView)itemView.findViewById(R.id.descTopHomePage);
            stt=(TextView)itemView.findViewById(R.id.sttTopHomePage);
        }
    }
}
