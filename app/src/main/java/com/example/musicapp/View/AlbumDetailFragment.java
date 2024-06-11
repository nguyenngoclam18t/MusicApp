package com.example.musicapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumDetailFragment extends Fragment implements OnSongClick {

    private static final String TAG = "AlbumDetailFragment";

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private List<SongModel> songList = new ArrayList<>();
    private ZingMp3Api zingMp3Api;
    private ExecutorService executorService;

    private TextView titlePlaylist;
    private ImageView imagePlaylist;

    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    public static AlbumDetailFragment newInstance(String playlistId) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putString("playlistId", playlistId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zingMp3Api = new ZingMp3Api();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_detail, container, false);

        recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        titlePlaylist = view.findViewById(R.id.TitlePlaylist);
        imagePlaylist = view.findViewById(R.id.imagePlaylist);

        if (getArguments() != null) {
            String playlistId = getArguments().getString("playlistId");
            loadSongsByPlaylist(playlistId);
        }

        return view;
    }

    private void loadSongsByPlaylist(String playlistId) {
        executorService.execute(() -> {
            try {
                JsonObject playlistData = zingMp3Api.getDetailPlaylist(playlistId);
                if (playlistData != null && playlistData.has("data")) {
                    JsonObject data = playlistData.getAsJsonObject("data");
                    String title = data.get("title").getAsString();
                    String thumbnail = data.get("thumbnailM").getAsString();
                    JsonArray songArray = data.getAsJsonObject("song").getAsJsonArray("items");

                    List<SongModel> songs = new ArrayList<>();
                    for (JsonElement element : songArray) {
                        JsonObject songObj = element.getAsJsonObject();

                        String encodeId = songObj.get("encodeId").getAsString();
                        String titleSong = songObj.get("title").getAsString();
                        String thumbnailSong = songObj.get("thumbnailM").getAsString();
                        String artistsNames = songObj.get("artistsNames").getAsString();
                        int duration = songObj.get("duration").getAsInt();

                        songs.add(new SongModel(encodeId, titleSong, artistsNames, thumbnailSong, "", duration));
                    }

                    getActivity().runOnUiThread(() -> {
                        titlePlaylist.setText(title);
                        Picasso.get().load(thumbnail).placeholder(R.drawable.img).into(imagePlaylist);
                        songList.clear();
                        songList.addAll(songs);
                        if (songAdapter == null) {
                            songAdapter = new SongAdapter(songList, getContext(), this);
                            recyclerView.setAdapter(songAdapter);
                        } else {
                            songAdapter.notifyDataSetChanged();
                        }
                    });

                } else {
                    Log.e(TAG, "Không tìm thấy dữ liệu playlistId: " + playlistId);
                }

            } catch (Exception e) {
                Log.e(TAG, "lỗi load playlist", e);
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
        intent.putExtra("duration", song.getDuration());
        startActivity(intent);
    }
}
