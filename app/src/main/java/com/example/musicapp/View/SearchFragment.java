package com.example.musicapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Controller.AlbumHomePageAdapter;
import com.example.musicapp.Controller.FarvoriteSingerAdapter;
import com.example.musicapp.Controller.TopMusicHomePageAdapter;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.OnArtistClick;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements OnArtistClick, OnSongClick, OnAlbumClick {

    private SearchView searchView;
    private TextView txtArtists, txtSong, txtAlbum;
    private ZingMp3Api zingMp3Api = new ZingMp3Api();
    private String searchString = "";
    private RecyclerView rclSong, rclArtists, rclAlbum;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize views
        searchView = view.findViewById(R.id.EdtSearchString);
        rclAlbum = view.findViewById(R.id.SearchRecycleViewAlbum);
        rclArtists = view.findViewById(R.id.SearchRecycleViewSinger);
        rclSong = view.findViewById(R.id.SearchRecycleViewSong);
        txtSong = view.findViewById(R.id.txtSearchSong);
        txtAlbum = view.findViewById(R.id.txtSearchAlbum);
        txtArtists = view.findViewById(R.id.txtSearchArtists);

        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchView.setQuery(searchView.getQuery(), true);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchString = query.trim();
                txtArtists.setText("");
                txtSong.setText("");
                txtAlbum.setText("");

                new Thread(SearchFragment.this::run).start();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    public void setAlbum(ArrayList<PlaylistModel> arr) {
        rclAlbum.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        AlbumHomePageAdapter adapterNewsToday = new AlbumHomePageAdapter(arr, this);
        rclAlbum.setAdapter(adapterNewsToday);
        if (!arr.isEmpty()) {
            txtAlbum.setText("Album");
        }
    }

    public void setSong(ArrayList<SongModel> arr) {
        rclSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        TopMusicHomePageAdapter adapterTop = new TopMusicHomePageAdapter(arr, this);
        rclSong.setAdapter(adapterTop);
        if (!arr.isEmpty()) {
            txtSong.setText("Song");
        }
    }

    public void setArtists(ArrayList<ArtistsModel> arr) {
        rclArtists.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        FarvoriteSingerAdapter adapterFavoriteSinger = new FarvoriteSingerAdapter(arr, this);
        rclArtists.setAdapter(adapterFavoriteSinger);
        if (!arr.isEmpty()) {
            txtArtists.setText("Artists");
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

    @Override
    public void OnAlbumClick(PlaylistModel album) {
        Bundle bundle = new Bundle();
        bundle.putString("albumId", album.getPlaylistId());
        bundle.putString("albumTitle", album.getPlaylistName());
        bundle.putString("albumThumbnail", album.getThumbnailLm());
        bundle.putString("albumDescription", album.getSortDescription());
        TopSongFragment topSongFragment = new TopSongFragment();
        topSongFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, topSongFragment).addToBackStack(null).commit();
    }

    @Override
    public void onArtistClick(ArtistsModel artist) {
        Bundle bundle = new Bundle();
        bundle.putString("artistId", artist.getArtistId());
        bundle.putString("alias", artist.getArtistAliasName());
        bundle.putString("artistName", artist.getArtistName());
        bundle.putString("thumbnailLm", artist.getThumbnailLm());
        ArtistProfileFragment fragment = new ArtistProfileFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, fragment).addToBackStack(null).commit();
    }

    private void run() {
        try {
            JsonObject songData = zingMp3Api.search(searchString);
            JsonObject itemsarr = songData.getAsJsonObject("data");
            JsonArray dataArtist = itemsarr.getAsJsonArray("artists");
            JsonArray dataSong = itemsarr.getAsJsonArray("songs");
            JsonArray dataAlbum = itemsarr.getAsJsonArray("playlists");

            ArrayList<ArtistsModel> artistsModelArrayList = new ArrayList<>();
            ArrayList<PlaylistModel> playlistModelArrayList = new ArrayList<>();
            ArrayList<SongModel> songModelArrayList = new ArrayList<>();

            // Parse artists
            for (JsonElement element : dataArtist) {
                JsonObject items = element.getAsJsonObject();
                String artistId = items.get("id").getAsString();
                String artistName = items.get("name").getAsString();
                String artistAliasName = items.get("alias").getAsString();
                String sortBiography = "";
                String thumbnailLm = items.get("thumbnailM").getAsString();
                artistsModelArrayList.add(new ArtistsModel(artistId, artistName, artistAliasName, sortBiography, thumbnailLm));
            }

            // Parse songs
            for (JsonElement element : dataSong) {
                JsonObject itemObj = element.getAsJsonObject();
                String encodeId = itemObj.get("encodeId").getAsString();
                String title = itemObj.get("title").getAsString();
                String thumbnailM = itemObj.get("thumbnailM").getAsString();
                String artistsNames = itemObj.get("artistsNames").getAsString();
                long timestamp = itemObj.get("releaseDate").getAsLong();
                int duration = itemObj.get("duration").getAsInt();
                songModelArrayList.add(new SongModel(encodeId, title, artistsNames, thumbnailM, "", duration));
            }

            // Parse albums
            for (JsonElement element : dataAlbum) {
                JsonObject itemObj = element.getAsJsonObject();
                String encodeId = itemObj.get("encodeId").getAsString();
                String title = itemObj.get("title").getAsString();
                String thumbnailM = itemObj.get("thumbnailM").getAsString();
                String sortDescription = itemObj.get("sortDescription").getAsString();
                playlistModelArrayList.add(new PlaylistModel(encodeId, title, thumbnailM, ""));
            }

            getActivity().runOnUiThread(() -> {
                setArtists(artistsModelArrayList);
                setAlbum(playlistModelArrayList);
                setSong(songModelArrayList);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
