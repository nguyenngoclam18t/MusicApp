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

public class NewsTodayAdapter extends RecyclerView.Adapter<NewsTodayAdapter.ViewHolder> {

    ArrayList<Singer> lst;

    public NewsTodayAdapter(ArrayList<Singer> lst) {
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
        holder.title.setText(lst.get(position).getName());
        holder.img.setImageResource(lst.get(position).getImage());

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
