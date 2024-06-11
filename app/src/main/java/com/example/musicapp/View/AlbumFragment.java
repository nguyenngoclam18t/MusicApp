package com.example.musicapp.View;

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

import com.example.musicapp.Controller.AlbumAdapter;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumFragment extends Fragment implements OnAlbumClick {

    private static final String TAG = "AlbumFragment";

    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private List<PlaylistModel> albumList = new ArrayList<>();
    private ZingMp3Api zingMp3Api = new ZingMp3Api();
    private ExecutorService executorService;

    public AlbumFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newSingleThreadExecutor();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        albumAdapter = new AlbumAdapter(albumList, this);
        recyclerView.setAdapter(albumAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String artistId = bundle.getString("artistId");
            loadAlbumsByArtist(artistId);
        }

        return view;
    }

    private void loadAlbumsByArtist(String artistId) {
        executorService.execute(() -> {
            try {
                JsonObject playlists = zingMp3Api.getListArtistPlaylist(artistId, "1", "10");
                Log.d(TAG, "Album Response: " + playlists.toString());
                if (playlists != null && playlists.has("data")) {
                    List<PlaylistModel> albums = new ArrayList<>();
                    getPlayList(playlists.getAsJsonObject("data"), albums);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            albumList.clear();
                            albumList.addAll(albums);
                            albumAdapter.notifyDataSetChanged();
                        });
                    }
                } else {
                    Log.e(TAG, "No playlists found");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading albums by artist", e);
            }
        });
    }

    private void getPlayList(JsonObject data, List<PlaylistModel> modelArrayList) {
        JsonArray arr = data.getAsJsonArray("items");
        for (JsonElement element : arr) {
            JsonObject itemObj = element.getAsJsonObject();
            String encodeId = itemObj.get("encodeId").getAsString();
            String title = itemObj.get("title").getAsString();
            String thumbnailM = itemObj.get("thumbnailM").getAsString();
            String sortDescription = itemObj.has("sortDescription") ? itemObj.get("sortDescription").getAsString() : "";

            JsonArray artistsArray = itemObj.getAsJsonArray("artists");
            List<ArtistsModel> artistsList = new ArrayList<>();
            for (JsonElement artistElement : artistsArray) {
                JsonObject artistObj = artistElement.getAsJsonObject();
                String artistName = artistObj.get("name").getAsString();
                String artistId = artistObj.get("id").getAsString();
                String artistThumbnail = artistObj.has("thumbnailM") ? artistObj.get("thumbnailM").getAsString() : "";
                artistsList.add(new ArtistsModel(artistId, artistName, "", "", artistThumbnail));
            }

            PlaylistModel playlistModel = new PlaylistModel(encodeId, title, thumbnailM, sortDescription, artistsList);
            modelArrayList.add(playlistModel);
        }
    }

    @Override
    public void OnAlbumClick(PlaylistModel album) {
        Bundle bundle = new Bundle();
        bundle.putString("playlistId", album.getPlaylistId());
        bundle.putString("playlistName", album.getPlaylistName());
        bundle.putString("ThumbnailM", album.getThumbnailLm());
        bundle.putString("sortDescription", album.getSortDescription());
        TopSongFragment topSongFragment = new TopSongFragment();
        topSongFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, topSongFragment).addToBackStack(null).commit();
    }
}

