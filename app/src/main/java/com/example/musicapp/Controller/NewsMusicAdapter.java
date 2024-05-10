package com.example.musicapp.Controller;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.Music;
import com.example.musicapp.R;

import java.util.ArrayList;

public class NewsMusicAdapter extends RecyclerView.Adapter<NewsMusicAdapter.ViewHolder> {
    ArrayList<Music> arr;

    public NewsMusicAdapter(ArrayList<Music> arr) {
        this.arr = arr;
    }
    @NonNull
    @Override
    public NewsMusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_card_recyclevie_homepage,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsMusicAdapter.ViewHolder holder, int position) {
        holder.img.setImageResource(arr.get(position).getImage());
        holder.title.setText(arr.get(position).getTitle());
        holder.singer.setText(arr.get(position).getSinger());
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