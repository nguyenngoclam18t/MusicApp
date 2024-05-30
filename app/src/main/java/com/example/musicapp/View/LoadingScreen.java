package com.example.musicapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.Model.FirestoreCallback;
import com.example.musicapp.R;

public class LoadingScreen extends AppCompatActivity implements FirestoreCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        FireStoreDB.initializeData(this);
    }
    @Override
    public void onCallback() {

        Intent intent = new Intent(LoadingScreen.this, Nav_Bar_Menu.class);
        startActivity(intent);
        finish();
    }
}