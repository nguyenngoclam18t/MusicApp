package com.example.musicapp.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsTodayAdapter extends RecyclerView.Adapter<NewsTodayAdapter.ViewHolder> {

    ArrayList<ArtistsModel> lst;

    public NewsTodayAdapter(ArrayList<ArtistsModel> lst) {
        this.lst = lst;
    }

    @NonNull
    @Override
    public NewsTodayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_newstoday,parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsTodayAdapter.ViewHolder holder, int position) {
        Picasso.get()
                .load(lst.get(position).avatarUrl)
                .placeholder(R.drawable.loading)
                .into(holder.img);
        holder.title.setText(lst.get(position).artistId);
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.titleNewsTodayHomePage);
            img=(ImageView) itemView.findViewById(R.id.imgNewsTodayHomePage);
        }
    }
}
