package com.example.musicapp.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.Controller.AlbumAdapter;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.ApiCollectionHomePage;
import com.example.musicapp.Model.FirestoreCallback;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private ArrayList<PlaylistModel> albumList = new ArrayList<>();
    private OnAlbumClick albumClick;

    public AlbumFragment(OnAlbumClick albumClick) {
        this.albumClick = albumClick;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAlbumsByArtist();
        return view;
    }

    private void loadAlbumsByArtist() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String artistId = bundle.getString("artistId");
//            ApiCollectionHomePage.getAlbumsByArtist(artistId, new FirestoreCallback() {
//                @Override
//                public void onCallback() {}
//
//                @Override
//                public void onSongsCallback(ArrayList<SongModel> songs) {}
//                @Override
//                public void onAlbumsCallback(ArrayList<PlaylistModel> albums) {
//                    albumList.clear();
//                    albumList.addAll(albums);
//                    if(albumAdapter == null) {
//                        albumAdapter = new AlbumAdapter(albumList, albumClick);
//                        recyclerView.setAdapter(albumAdapter);
//                    }
//                    else {
//                        albumAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onArtistsCallback(ArrayList<ArtistsModel> artistsList) {}
//            });
        }
    }


}
