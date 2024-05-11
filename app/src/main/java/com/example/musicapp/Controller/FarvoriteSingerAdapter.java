package com.example.musicapp.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.Singer;
import com.example.musicapp.R;

import java.util.ArrayList;

public class FarvoriteSingerAdapter extends RecyclerView.Adapter<FarvoriteSingerAdapter.ViewHolder> {
    ArrayList<Singer> arr;

    public FarvoriteSingerAdapter(ArrayList<Singer> arr) {
        this.arr = arr;
    }

    @NonNull
    @Override
    public FarvoriteSingerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_favoritesinger,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FarvoriteSingerAdapter.ViewHolder holder, int position) {
        holder.img.setImageResource(arr.get(position).getImage());
        holder.title.setText(arr.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=(ImageView) itemView.findViewById(R.id.imgFarvoriteSingerHP);
            title=(TextView) itemView.findViewById(R.id.titleFarvoriteSingerHP);
        }
    }
}
