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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.musicapp.Controller.NewsMusicAdapter;
import com.example.musicapp.Controller.AlbumHomePageAdapter;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.ApiCollectionHomePage;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.OnArtistClick;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class HomePageFragment extends Fragment implements OnArtistClick, OnSongClick, OnAlbumClick {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //my varible
    RecyclerView recyclerViewNewsRelease;
    RecyclerView recyclerViewChillPlayList;
    RecyclerView recyclerViewSummerPlayList;
    RecyclerView recyclerViewHotPlayList;
    RecyclerView recyclerViewRemixPlayList;
    LinearLayout suggestSingerLayout;

    ImageSlider imageSlider;
    ZingMp3Api zingMp3Api=new ZingMp3Api();
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

    private void effectNewsRelease() {
        ArrayList<SongModel> arrNewsSongs = new ArrayList<>(ApiCollectionHomePage.arrSongNewRelease.subList(0, Math.min(ApiCollectionHomePage.arrSongNewRelease.size(), 15)));
        RecyclerView.Adapter adapterNewsMusic=new NewsMusicAdapter(arrNewsSongs, this);
        recyclerViewNewsRelease.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNewsRelease.setAdapter(adapterNewsMusic);


    }

    private void effectSlider() {
        // slider
        ArrayList<SlideModel> arrSlider = new ArrayList<>();
        for (String item: ApiCollectionHomePage.arrBanner) {
            arrSlider.add(new SlideModel(item, ScaleTypes.CENTER_CROP));
        }
        imageSlider.setImageList(arrSlider, ScaleTypes.CENTER_CROP);
    }

    private void effectNewsMusic( RecyclerView recycler, ArrayList<PlaylistModel> arrNewsSongs ) {
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.Adapter adapterPlayList;
        adapterPlayList = new AlbumHomePageAdapter(arrNewsSongs,this);
        recycler.setAdapter(adapterPlayList);
    }
    private void effectSuggestSinger(View view){
        String[] artistArray = {"Huong-Ly", "Jombie", "Double2T", "Hoa-Minzy", "HIEUTHUHAI", "Son-Tung-M-TP"};
        Random random = new Random();
        int randomIndex = random.nextInt(artistArray.length);
        String randomArtist = artistArray[randomIndex];

        ImageView img = view.findViewById(R.id.imgSuggestSingerHomepage);
        TextView nameSinger = view.findViewById(R.id.nameSuggestSingerHomepage);
        TextView descSinger = view.findViewById(R.id.descSuggestSingerHomepage);
        LinearLayout suggestSingerLayout = view.findViewById(R.id.suggestSingerLayout);

        new Thread(() -> {
            try {
                JsonObject songData = zingMp3Api.getArtist(randomArtist);
                JsonObject items = songData.getAsJsonObject("data");
                String artistId = items.get("id").getAsString();
                String artistName = items.get("name").getAsString();
                String artistAliasName = items.get("alias").getAsString();
                String sortBiography = items.get("sortBiography").getAsString();
                String thumbnailLm = items.get("thumbnailM").getAsString();

                ArtistsModel artistsModel = new ArtistsModel(artistId, artistName, artistAliasName, sortBiography, thumbnailLm);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Picasso.get()
                                .load(artistsModel.getThumbnailLm())
                                .into(img);
                        nameSinger.setText(artistsModel.getArtistName());

                        String shortBiography = artistsModel.getSortBiography();
                        if (shortBiography.length() > 70) {
                            shortBiography = shortBiography.substring(0, 70) + "...";
                        }else if(shortBiography.isEmpty()){
                            shortBiography="Nghệ Sĩ này chưa cập nhật mô tả về bản thân.";
                        }
                        descSinger.setText(shortBiography);

                        suggestSingerLayout.setOnClickListener(v -> onArtistClick(artistsModel));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        imageSlider = (ImageSlider) view.findViewById(R.id.imgSliderHomePage);
        recyclerViewNewsRelease = (RecyclerView) view.findViewById(R.id.recyclerViewNewsRelease);
        recyclerViewChillPlayList = (RecyclerView) view.findViewById(R.id.RecyclerViewChillPlayList);
        recyclerViewSummerPlayList = (RecyclerView) view.findViewById(R.id.RecyclerViewSummerPlayList);
        recyclerViewRemixPlayList = (RecyclerView) view.findViewById(R.id.RecyclerViewRemixPlayList);
        recyclerViewHotPlayList = (RecyclerView) view.findViewById(R.id.RecyclerViewHotPlayList);

        effectSlider();
        effectNewsRelease();
        effectNewsMusic(recyclerViewChillPlayList,ApiCollectionHomePage.arrPlaylistChill);
        effectNewsMusic(recyclerViewSummerPlayList,ApiCollectionHomePage.arrPlaylistSummer);
        effectNewsMusic(recyclerViewRemixPlayList,ApiCollectionHomePage.arrPlaylistRemix);
        effectNewsMusic(recyclerViewHotPlayList,ApiCollectionHomePage.arrPlaylistHot);
        effectSuggestSinger(view);
        return view;
    }

    @Override
    public void onSongClick(SongModel song) {
        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra("songId", song.getSongId());
        startActivity(intent);
    }
    @Override
    public void OnAlbumClick(PlaylistModel album) {
        Bundle bundle = new Bundle();
        bundle.putString("albumId", album.getPlaylistId());
        TopSongFragment topSongFragment=new TopSongFragment();
        topSongFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, topSongFragment).addToBackStack(null).commit();
    }
    public void onArtistClick(ArtistsModel artists) {
        Bundle bundle = new Bundle();
        bundle.putString("artistId", artists.getArtistId());
        ArtistProfileFragment artistProfileFragment = new ArtistProfileFragment();
        artistProfileFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, artistProfileFragment).addToBackStack(null).commit();
    }
}