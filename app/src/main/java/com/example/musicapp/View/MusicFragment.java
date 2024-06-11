package com.example.musicapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Controller.SongAdapter;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicFragment extends Fragment implements OnSongClick {

    private static final String TAG = "MusicFragment";

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ArrayList<SongModel> songList = new ArrayList<>();
    private ZingMp3Api zingMp3Api;
    private ExecutorService executorService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zingMp3Api = new ZingMp3Api();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        songAdapter = new SongAdapter(songList, getContext(), this);
        recyclerView.setAdapter(songAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String artistId = bundle.getString("artistId");
            loadSongsByArtist(artistId);
        }

        return view;
    }

    private void loadSongsByArtist(String artistId) {
        executorService.execute(() -> {
            try {
                JsonObject jsonObject = zingMp3Api.getListArtistSong(artistId, "1", "30");
                Log.d(TAG, "Song Response: " + jsonObject.toString());
                if (jsonObject != null && jsonObject.has("data")) {
                    JsonObject dataObject = jsonObject.getAsJsonObject("data");
                    int total = dataObject.get("total").getAsInt();
                    if (total > 0 && dataObject.has("items")) {
                        JsonArray songArray = dataObject.getAsJsonArray("items");
                        songList.clear();
                        for (JsonElement songElement : songArray) {
                            JsonObject songObject = songElement.getAsJsonObject();
                            String id = songObject.get("encodeId").getAsString();
                            String name = songObject.get("title").getAsString();
                            String artistName = songObject.get("artistsNames").getAsString();
                            String thumbnail = songObject.has("thumbnailM") ? songObject.get("thumbnailM").getAsString() : "";
                            String songUrl = "";

                            if (songObject.has("album")) {
                                JsonObject albumObject = songObject.getAsJsonObject("album");
                                songUrl = "https://zingmp3.vn" + albumObject.get("link").getAsString();
                            } else {
                                songUrl = "https://zingmp3.vn" + songObject.get("link").getAsString();
                            }

                            int duration = songObject.get("duration").getAsInt();

                            SongModel song = new SongModel(id, name, artistName, thumbnail, songUrl, duration);
                            songList.add(song);
                        }
                        getActivity().runOnUiThread(() -> {
                            if (songAdapter == null) {
                                songAdapter = new SongAdapter(songList, getContext(), this);
                                recyclerView.setAdapter(songAdapter);
                            } else {
                                songAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Log.e(TAG, "Không tìm thấy bài hát");
                    }
                } else {
                    Log.e(TAG, "Dữ liệu bài hát không tồn tại");
                }
            } catch (Exception e) {
                Log.e(TAG, "Lỗi khi loading: ", e);
            }
        });
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
