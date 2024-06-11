package com.example.musicapp.View;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapp.Controller.TopMusicHomePageAdapter;
import com.example.musicapp.Model.ApiCollectionHomePage;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopSongFragment extends Fragment implements OnSongClick {

    private RecyclerView recyclerViewTopSongFragment;
    private TextView titlePlayList;
    private ImageView img;
    private ZingMp3Api zingMp3Api = new ZingMp3Api();

    public TopSongFragment() {
        // Required empty public constructor
    }

    public static TopSongFragment newInstance(String param1, String param2) {
        TopSongFragment fragment = new TopSongFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public void getTopToRecycleView() {
        Bundle bundle = getArguments();
        String albumId;
        if (bundle != null) {
            albumId = bundle.getString("albumId");
        } else {
            albumId = "Z6CZO0F6";
        }
        new Thread(() -> {
            try {
                ArrayList<SongModel> arrTop = new ArrayList<>();
                JsonObject songData = zingMp3Api.getDetailPlaylist(albumId);
                JsonObject items = songData.getAsJsonObject("data");

                String titlePlaylist = items.get("title").getAsString();
                String thumbnailMPlayList = items.get("thumbnailM").getAsString();
                JsonObject Song = items.getAsJsonObject("song");
                JsonArray arrSong = Song.getAsJsonArray("items");
                for (JsonElement element : arrSong) {
                    JsonObject itemObj = element.getAsJsonObject();

                    String encodeId = itemObj.get("encodeId").getAsString();
                    String title = itemObj.get("title").getAsString();
                    String thumbnailM = itemObj.get("thumbnailM").getAsString();
                    String artistsNames = itemObj.get("artistsNames").getAsString();
                    long timestamp = itemObj.get("releaseDate").getAsLong();
                    Date date = new Date(timestamp * 1000);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = sdf.format(date);
                    int duration = itemObj.get("duration").getAsInt();
                    arrTop.add(new SongModel(encodeId, title, artistsNames,  thumbnailM,"", duration));
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Picasso.get()
                                .load(thumbnailMPlayList)
                                .placeholder(R.drawable.todays_top_hits)
                                .into(img);
                        titlePlayList.setText(titlePlaylist);
                        RecyclerView.Adapter adapterTop;
                        adapterTop = new TopMusicHomePageAdapter(arrTop, this);
                        recyclerViewTopSongFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        recyclerViewTopSongFragment.setAdapter(adapterTop);
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
        View view = inflater.inflate(R.layout.fragment_top_song, container, false);
        recyclerViewTopSongFragment = view.findViewById(R.id.RecyclerViewTopSongFragment);
        img = view.findViewById(R.id.imageBackground);
        titlePlayList = view.findViewById(R.id.TitlePlaylistTopSong);

        if (getArguments() != null) {
            updateData(getArguments());
        }

        return view;
    }

    public void updateData(Bundle bundle) {
        if (bundle != null) {
            String albumId = bundle.getString("albumId");
            getTopToRecycleView();
        }
    }

    @Override
    public void onSongClick(SongModel song) {
        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra("encodeId", song.getSongId());
        intent.putExtra("title", song.getTitle());
        intent.putExtra("thumbnailM", song.getThumbnailLm());
        intent.putExtra("artistsNames", song.getArtistsNames());
        intent.putExtra("link", song.getSongUrl());
        intent.putExtra("duration", song.getDuration());
        startActivity(intent);
    }
}
