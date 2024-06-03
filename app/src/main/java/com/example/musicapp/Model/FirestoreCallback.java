package com.example.musicapp.Model;

import java.util.ArrayList;

public interface  FirestoreCallback {
    void onCallback();
    void onSongsCallback(ArrayList<SongModel> songs);
    void onAlbumsCallback(ArrayList<AlbumModel> album);
    void onArtistsCallback(ArrayList<ArtistsModel> artistsList);
}
