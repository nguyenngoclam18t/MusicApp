package com.example.musicapp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Controller.SongAdapter;
import com.example.musicapp.Model.AlbumModel;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.Model.FirestoreCallback;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MusicFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ArrayList<SongModel> songList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadSongsByArtist();
        return view;
    }

    private void loadSongsByArtist() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String artistId = bundle.getString("artistId");
            FireStoreDB.getSongsByArtist(artistId, new FirestoreCallback() {
                @Override
                public void onCallback() {

                }

                @Override
                public void onSongsCallback(ArrayList<SongModel> songs) {
                    songList.clear();
                    songList.addAll(songs);
                    if (songAdapter == null) {
                        songAdapter = new SongAdapter(songList);
                        recyclerView.setAdapter(songAdapter);
                    } else {
                        songAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onAlbumsCallback(ArrayList<AlbumModel> album) {

                }

                @Override
                public void onArtistsCallback(ArrayList<ArtistsModel> artistsList) {

                }
            });
        }
    }
}
