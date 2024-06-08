package com.example.musicapp.Controller;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapp.Model.GenreModel;
import com.example.musicapp.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GenreSongAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GenreModel> mDataList;

    public GenreSongAdapter(Context context, ArrayList<GenreModel> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.viewholder_genres_song, parent, false);
//            viewHolder = new ViewHolder();
//            viewHolder.textView = convertView.findViewById(R.id.titleGenre);
//            viewHolder.img = convertView.findViewById(R.id.imageGenre);
//            viewHolder.Card = convertView.findViewById(R.id.GenreCard);
//            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String text = mDataList.get(position).GenreName;
//        viewHolder.textView.setText(text);
        String[] arrColors = {
                "#FBA3D1",
                "#EBA3FB",
                "#BC99FD",
                "#A3AFFB",
                "#A3CEFB",
                "#A3F8FB",
                "#A3FBCB",
                "#ACFBA3",
                "#FBF7A3",
                "#F8947A",
                "#FF7C7C"
        };
        int positionColor = position % arrColors.length;
        String colorString = arrColors[positionColor];
//        viewHolder.Card.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorString)));
        String imageName = mDataList.get(position).GenreName.toLowerCase(Locale.ROOT); // Tên ảnh lưu trong drawable
        int imageResId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        if (imageResId != 0) {
//            viewHolder.img.setImageResource(imageResId);
        } else {
//            viewHolder.img.setImageResource(R.drawable.son_tung_mtp);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        MaterialCardView Card;
        ImageView img;
    }
}
