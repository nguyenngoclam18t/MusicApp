package com.example.musicapp.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.musicapp.Controller.FarvoriteSingerAdapter;
import com.example.musicapp.Controller.NewsMusicAdapter;
import com.example.musicapp.Controller.NewsTodayAdapter;
import com.example.musicapp.Controller.TopMusicHomePageAdapter;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.Model.OnArtistClick;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePageFragment extends Fragment implements OnArtistClick, OnSongClick {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //my varible
    RecyclerView recyclerViewNewsToday;
    RecyclerView recyclerViewNewsMusic;
    RecyclerView recyclerViewTopMusic;
    RecyclerView recyclerViewFavoriteSinger;

    ImageSlider imageSlider;

    public HomePageFragment() {
        // Required empty public constructor
    }
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void effectNewsToday() {

        recyclerViewNewsToday.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.Adapter adapterNewsToday;
        adapterNewsToday = new NewsTodayAdapter(FireStoreDB.arrArtists, this);
        recyclerViewNewsToday.setAdapter(adapterNewsToday);

    }

    private void effectSlider() {
        // slider
        ArrayList<SlideModel> arrSlider = new ArrayList<>();
        arrSlider.add(new SlideModel(R.drawable.sample_slider1, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider2, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider1, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider2, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider1, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider2, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(arrSlider, ScaleTypes.CENTER_CROP);
    }

    private void effectNewsMusic() {
        ArrayList<SongModel> arrNewsSongs = new ArrayList<>(FireStoreDB.arrSong.subList(0, Math.min(FireStoreDB.arrSong.size(), 15)));
        RecyclerView.Adapter adapterNewsMusic=new NewsMusicAdapter(arrNewsSongs, this);
        recyclerViewNewsMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNewsMusic.setAdapter(adapterNewsMusic);

    }
    private void effectSuggestSinger(View view){
        ImageView img;
        TextView nameSinger,descSinger;
        img=(ImageView) view.findViewById(R.id.imgSuggestSingerHomepage);
        nameSinger=(TextView) view.findViewById(R.id.nameSuggestSingerHomepage);
        descSinger=(TextView) view.findViewById(R.id.descSuggestSingerHomepage);
        Picasso.get()
                .load(FireStoreDB.arrArtists.get(0).getAvatarUrl())
                .into(img);
        nameSinger.setText(FireStoreDB.arrArtists.get(0).artistId);
        descSinger.setText("đây là 1 ca sĩ trẻ đầy  tài năng và mang đột phá trong gout âm nhạc của mình.");
    }
    private  void effectTopHomePage(){
        RecyclerView.Adapter adapterTop;
        ArrayList<SongModel> arrTop = new ArrayList<>(FireStoreDB.arrSong.subList(0, Math.min(FireStoreDB.arrSong.size(), 5)));
        adapterTop=new TopMusicHomePageAdapter(arrTop);
        recyclerViewTopMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTopMusic.setAdapter(adapterTop);
    }
    private  void effectFavoriteSinger(){
        RecyclerView.Adapter adapterFavoriteSinger;
        adapterFavoriteSinger=new FarvoriteSingerAdapter(FireStoreDB.arrArtists, this);
        recyclerViewFavoriteSinger.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFavoriteSinger.setAdapter(adapterFavoriteSinger);
    }
    private void getArray(View view){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        imageSlider = (ImageSlider) view.findViewById(R.id.imgSliderHomePage);
        recyclerViewNewsToday = (RecyclerView) view.findViewById(R.id.RecyclerViewNewsToday);
        recyclerViewNewsMusic = (RecyclerView) view.findViewById(R.id.RecyclerViewNewsMusic);
        recyclerViewTopMusic = (RecyclerView) view.findViewById(R.id.RecyclerViewTopMusic);
        recyclerViewFavoriteSinger=(RecyclerView) view.findViewById(R.id.RecyclerViewFarvoriteSinger);
        getArray(view);
        effectSlider();
        effectNewsMusic();
        effectTopHomePage();
        effectSuggestSinger(view);
        effectFavoriteSinger();
        effectNewsToday();
        return view;
    }

    @Override
    public void onSongClick(SongModel song) {
        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra("songId", song.getSongId());
        startActivity(intent);
    }

    public void onArtistClick(ArtistsModel artists) {
        Bundle bundle = new Bundle();
        bundle.putString("artistId", artists.getArtistId());
        bundle.putString("artistName", artists.getArtistName());
        bundle.putString("avatarUrl", artists.getAvatarUrl());
        ArtistProfileFragment artistProfileFragment = new ArtistProfileFragment();
        artistProfileFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, artistProfileFragment).addToBackStack(null).commit();
    }
}