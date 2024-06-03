package com.example.musicapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.musicapp.Model.AlbumModel;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.Model.FirestoreCallback;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import java.util.ArrayList;

public class LoadingScreen extends AppCompatActivity implements FirestoreCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        FireStoreDB.initializeData(this);
    }
    @Override
    public void onCallback() {
        preloadImages();
        Intent intent = new Intent(LoadingScreen.this, Nav_Bar_Menu.class);
        startActivity(intent);
        finish();
    }
    private  void preloadImages(){
        for (AlbumModel model:FireStoreDB.arrAlbum ) {
            loadImage(model.imageUrl);
        }
        for (ArtistsModel model:FireStoreDB.arrArtists ) {
            loadImage(model.getAvatarUrl());
        }
    }
    private void loadImage(String imageUrls) {
        Picasso.get().load(imageUrls).fetch();
    }

    @Override
    public void onSongsCallback(ArrayList<SongModel> songs) {

    }

    @Override
    public void onAlbumsCallback(ArrayList<AlbumModel> album) {

    }

    @Override
    public void onArtistsCallback(ArrayList<ArtistsModel> artists) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}