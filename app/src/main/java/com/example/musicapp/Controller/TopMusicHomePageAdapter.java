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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TopMusicHomePageAdapter extends RecyclerView.Adapter<TopMusicHomePageAdapter.ViewHolder> {
    ArrayList<Music> arr;

    public TopMusicHomePageAdapter(ArrayList<Music> arr) {
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
        holder.img.setImageResource(arr.get(position).getImage());
        holder.title.setText(arr.get(position).getTitle());
        holder.desc.setText(arr.get(position).getSinger());
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
