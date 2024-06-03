package com.example.musicapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";

    TextView songTitle, artist_Name, durationPlayed, durationTotal;
    ImageButton btnPlay, btnRepeat, btnShuffle, btnFav, btnPrev, btnNext;
    ImageView songImage;
    SeekBar seekBar;
    private String songId;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();

        songId = getIntent().getStringExtra("songId");
        if (songId == null) {
            Toast.makeText(this, "No song selected", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            getData();
        }
    }

    private void initViews() {
        songImage = findViewById(R.id.song_img);
        songTitle = findViewById(R.id.song_title);
        artist_Name = findViewById(R.id.artist_name);
        durationPlayed = findViewById(R.id.drationPlayed);
        durationTotal = findViewById(R.id.drationTotal);
        seekBar = findViewById(R.id.seekbar);
        btnPlay = findViewById(R.id.play_pause);
        btnRepeat = findViewById(R.id.id_repeat);
        btnShuffle = findViewById(R.id.id_shuffle);
        btnFav = findViewById(R.id.btnFav);
        btnPrev = findViewById(R.id.id_prev);
        btnNext = findViewById(R.id.id_next);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                } else {
                    btnPlay.setImageResource(R.drawable.pause);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic to play next song
                // Example: playNextSong();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic to play previous song
                // Example: playPreviousSong();
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle repeat mode
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle shuffle mode
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress * 1000); // convert to milliseconds
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void getData() {
        db.collection("songs").document(songId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        SongModel song = documentSnapshot.toObject(SongModel.class);
                        if (song != null) {
                            songTitle.setText(song.getTitle());
                            Picasso.get().load(song.getImgUrl()).into(songImage);
                            getArtistName(song.getArtistId());
                            playSong(song.getSongUrl());
                        }
                    } else {
                        Log.d(TAG, "Document does not exist");
                    }
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });
    }

    private void getArtistName(String artistId) {
        db.collection("artists").document(artistId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String artistName = documentSnapshot.getString("artistName");
                        if (artistName != null) {
                            artist_Name.setText(artistName);
                        }
                    } else {
                        Log.d(TAG, "Artist document does not exist");
                    }
                } else {
                    Log.d(TAG, "Error getting artist document: ", task.getException());
                }
            }
        });
    }

    private void playSong(String songUrl) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000); // convert to seconds
            updateSeekBar();
        } catch (IOException e) {
            Log.e(TAG, "Error playing song: ", e);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

    }

    private void updateSeekBar() {
        handler.postDelayed(updateTimeTask, 1000); // 1 second
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            durationTotal.setText(getDuration(totalDuration));
            durationPlayed.setText(getDuration(currentDuration));

            int progress = (int) (currentDuration / 1000);
            seekBar.setProgress(progress);

            handler.postDelayed(this, 1000); // 1 second
        }
    };

    private String getDuration(long duration) {
        long seconds = duration / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(updateTimeTask);
        }

    }
}
