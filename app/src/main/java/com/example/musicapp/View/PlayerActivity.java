package com.example.musicapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.musicapp.R;

public class PlayerActivity extends AppCompatActivity {

    TextView tvTitle, tvArtistName;
    ImageButton btnPlay, btnRepeat, btnShuffle, btnFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        addControl();
        addEvents();
    }

    public void addControl() {

    }

    public void addEvents() {

    }
}