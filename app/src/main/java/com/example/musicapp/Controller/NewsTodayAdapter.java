package com.example.musicapp.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.AlbumModel;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsTodayAdapter extends RecyclerView.Adapter<NewsTodayAdapter.ViewHolder> {

    private final ArrayList<AlbumModel> lst;

    public NewsTodayAdapter(ArrayList<AlbumModel> lst) {
        this.lst = lst;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_newstoday, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = lst.get(position).imgUrl;
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.loading)
                .into(holder.img);
        holder.title.setText(lst.get(position).albumId);
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleNewsTodayHomePage);
            img = itemView.findViewById(R.id.imgNewsTodayHomePage);
        }
    }
}
