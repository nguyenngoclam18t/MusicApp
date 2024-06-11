package com.example.musicapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.media.session.MediaButtonReceiver;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.Controller.NotificationReceiver;
import com.example.musicapp.Model.ApiCollectionHomePage;
import com.example.musicapp.Model.DataProfilePage;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "music_player_channel";
    private static final int NOTIFICATION_ID = 1;
    private TextView songTitle, artistName, durationPlayed, durationTotal;
    private ImageButton btnPlay, btnRepeat, btnShuffle, btnFav, btnPrev, btnNext, btnBack;
    private ImageView songImage;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    private List<SongModel> songList = new ArrayList<>();
    private ZingMp3Api zingMp3Api = new ZingMp3Api(); // Initialize your API instance here
    private ExecutorService executorService;
    private SongModel currentSong;
    private MediaSessionCompat mediaSession;
    public static PlayerActivity instance;
    private  Context context;
    private static final String TAG = "PlayerActivity";

    private List<SongModel> songsList;
    private int currentSongIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler();

        if (getIntent() != null && getIntent().hasExtra("encodeId")) {
            String encodeId = getIntent().getStringExtra("encodeId");
            String title = getIntent().getStringExtra("title");
            String thumbnail = getIntent().getStringExtra("thumbnailM");
            String artistsNames = getIntent().getStringExtra("artistsNames");
            int duration = getIntent().getIntExtra("duration", 0);

            Log.d(TAG, "Received song details: encodeId=" + encodeId + ", title=" + title + ", thumbnail=" + thumbnail + ", artistsNames=" + artistsNames + ", duration=" + duration);

            currentSong = new SongModel(encodeId, title, artistsNames, thumbnail, "", duration);
            songList.add(currentSong);

            updateUI(currentSong);
            getSongUrl(encodeId);
        } else {
            Toast.makeText(this, "Không có bài hát được chọn", Toast.LENGTH_SHORT).show();
            finish();
        }
        getListSong(100);
    }

    private void initViews() {
        songImage = findViewById(R.id.song_img);
        songTitle = findViewById(R.id.song_title);
        artistName = findViewById(R.id.artist_name);
        durationPlayed = findViewById(R.id.durationPlayed);
        durationTotal = findViewById(R.id.durationTotal);
        seekBar = findViewById(R.id.seekbar);

        btnPlay = findViewById(R.id.play_pause);
        btnRepeat = findViewById(R.id.btn_repeat);
        btnShuffle = findViewById(R.id.btn_shuffle);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousSong();
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeat = !isRepeat;
                btnRepeat.setImageResource(isRepeat ? R.drawable.repeat_icon_green : R.drawable.repeat_icon_white);
                if (isRepeat) {
                    isShuffle = false;
                    btnShuffle.setImageResource(R.drawable.shuffle_icon_white);
                }
            }
        });
        btnShuffle.setOnClickListener(v -> {
            isShuffle = !isShuffle;
            btnShuffle.setImageResource(isShuffle ? R.drawable.shuffle_icon_green : R.drawable.shuffle_icon_white);
            if (isShuffle) {
                isRepeat = false;
                btnRepeat.setImageResource(R.drawable.repeat_icon_white);
            }
        });
    }
    private void getSongUrl(String encodeId) {
        Log.d(TAG, "Loading song URL for encodeId: " + encodeId);
        executorService.execute(() -> {
            try {
                JsonObject songJson = zingMp3Api.getSong(encodeId);
                if (songJson != null && songJson.has("data")) {
                    JsonObject dataObject = songJson.getAsJsonObject("data");
                    if (dataObject.has("128")) {
                        String songUrl = dataObject.get("128").getAsString();
                        runOnUiThread(() -> {
                            currentSong.setSongUrl(songUrl);
                            playSong(songUrl);
                        });
                    } else {
                    }
                } else {
                }
            } catch (Exception e) {
            }
        });
    }
    private void getListSong(int limit) {
        executorService.execute(() -> {
            try {
                JsonObject songData = zingMp3Api.getHome();
                Log.d(TAG, "Dữ liệu bài hát: " + songData.toString());
                if (songData != null) {
                    JsonObject itemsObj = songData.getAsJsonObject("data");
                    if (itemsObj != null && itemsObj.has("items")) {
                        JsonArray itemsArray = itemsObj.getAsJsonArray("items");
                        addToPlaylist(itemsArray);
                        Log.d(TAG, "Số lượng bài hát đã được thêm vào songList: " + songList.size());
                        if (songList.isEmpty()) {
                            runOnUiThread(() -> Toast.makeText(this, "Không có bài hát nào được tìm thấy", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Log.d(TAG, "Không có dữ liệu bài hát");
                    }
                } else {
                    Log.d(TAG, "Dữ liệu bài hát rỗng");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Lỗi khi lấy danh sách bài hát", e);
            }
        });
    }

    private void addToPlaylist(JsonArray itemsArray) {
        for (JsonElement itemElement : itemsArray) {
            JsonObject item = itemElement.getAsJsonObject();
            if (item.has("items")) {
                JsonObject sectionItems = item.getAsJsonObject("items");
                if (sectionItems.has("all")) {
                    JsonArray allSongs = sectionItems.getAsJsonArray("all");
                    for (JsonElement songElement : allSongs) {
                        JsonObject song = songElement.getAsJsonObject();
                        String title = song.get("title").getAsString();
                        String artist = song.getAsJsonArray("artistsNames").get(0).getAsString();
                        String encodeId = song.get("encodeId").getAsString();
                        String thumbnailM = song.get("thumbnailM").getAsString();
                        int duration = song.get("duration").getAsInt();
                        SongModel songModel = new SongModel(encodeId, title, artist, thumbnailM, "", duration);
                        songList.add(songModel);
                    }
                }
            }
        }
    }

    private void updateUI(SongModel song) {
        songTitle.setText(song.getTitle());
        artistName.setText(song.getArtistsNames());
        Picasso.get().load(song.getThumbnailLm()).into(songImage);
        durationTotal.setText(getDuration(song.getDuration()));
    }

    public void play() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.play);
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
            }
        }
    }

    private void playSong(String songUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                updateSeekBar();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                playNextSong();
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi phát nhạc", Toast.LENGTH_SHORT).show();
        }

        createNotification();
    }


    private void playNextSong() {
        if (songsList != null && !songsList.isEmpty()) {
            currentSongIndex++;
            if (currentSongIndex >= songsList.size()) {
                currentSongIndex = 0;
            }
            SongModel nextSong = songsList.get(currentSongIndex);
            String songUrl = nextSong.getSongUrl();
            playSong(songUrl);
        } else {
            // Handle the case where the list is empty
            Toast.makeText(this, "Không có bài hát để phát tiếp", Toast.LENGTH_SHORT).show();
            Log.e("PlayerActivity", "Danh sách bài hát trống.");
        }
    }

    private void playPreviousSong() {
        if (songList != null && !songList.isEmpty()) {
            currentSongIndex--;
            if (currentSongIndex < 0) {
                currentSongIndex = songList.size() - 1;
            }
            SongModel currentSong = songList.get(currentSongIndex);
            updateUI(currentSong);
            getSongUrl(currentSong.getSongId());
        } else {

            Toast.makeText(this, "Không có bài hát để phát", Toast.LENGTH_SHORT).show();
            Log.e("PlayerActivity", "Danh sách bài hát trống.");
        }
    }

    private void updateSeekBar() {
        if (mediaPlayer != null) {
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        durationPlayed.setText(getDuration(mCurrentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private String getDuration(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void createNotification() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            mediaSession = new MediaSessionCompat(this, "tag");

            PendingIntent pendingIntentPrevious;
            int drw_previous;
            if (currentSongIndex > 0) {
                Intent intentPrevious = new Intent(this, NotificationReceiver.class).setAction("previous");
                pendingIntentPrevious = PendingIntent.getBroadcast(this, 0, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                drw_previous = R.drawable.mini_prev;
            } else {
                pendingIntentPrevious = null;
                drw_previous = R.drawable.mini_prev;
            }

            Intent intentPlay = new Intent(this, NotificationReceiver.class).setAction("play");
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(this, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            Intent intentNext = new Intent(this, NotificationReceiver.class).setAction("next");
            PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            Picasso.get()
                    .load(currentSong.getThumbnailLm())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Notification notification = new NotificationCompat.Builder(PlayerActivity.this, CHANNEL_ID)
                                    .setLargeIcon(bitmap)
                                    .setSmallIcon(R.drawable.icon_music_note)
                                    .setContentTitle(songTitle.getText())
                                    .setContentText(artistName.getText())
                                    .setPriority(NotificationCompat.PRIORITY_LOW)
                                    .setOnlyAlertOnce(true)
                                    .setShowWhen(false)
                                    .addAction(drw_previous, "Previous", pendingIntentPrevious)
                                    .addAction(R.drawable.mini_pause, "Play", pendingIntentPlay)
                                    .addAction(R.drawable.mini_next, "Next", pendingIntentNext)
                                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                            .setShowActionsInCompactView(0, 1, 2)
                                            .setMediaSession(mediaSession.getSessionToken()))
                                    .setColor(getResources().getColor(R.color.primary)) // Set your custom color here
                                    .build();

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PlayerActivity.this);
                            notificationManager.notify(NOTIFICATION_ID, notification);
                        }
                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateNotification(String songTitle, String artistName) {
        if (mediaSession != null && mediaSession.isActive()) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            createNotificationChannel();

            Picasso.get()
                    .load(currentSong.getThumbnailLm())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(PlayerActivity.this, CHANNEL_ID);
                            builder.setContentTitle(songTitle)
                                    .setContentText(artistName)
                                    .setSmallIcon(R.drawable.icon_music_note)
                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setContentIntent(createPendingIntent())
                                    .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(PlayerActivity.this, PlaybackStateCompat.ACTION_STOP))
                                    .setOnlyAlertOnce(true)
                                    .setShowWhen(false)
                                    .setLargeIcon(bitmap) // Set large icon here
                                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                            .setMediaSession(mediaSession.getSessionToken())
                                            .setShowActionsInCompactView(0)
                                            .setShowCancelButton(true)
                                            .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(PlayerActivity.this, PlaybackStateCompat.ACTION_STOP)));

                            if (ActivityCompat.checkSelfPermission(PlayerActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            notificationManager.notify(NOTIFICATION_ID, builder.build());
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });

        } else {
            // Handle case where mediaSession is null or inactive
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }


    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Music Player";
            String description = "Music Player Notification Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onStop() {
        }
    }

    private void updateNotificationPlaybackState(int state) {
        if (mediaSession == null || !mediaSession.isActive()) {
            return;
        }

        PlaybackStateCompat.Builder playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                );

        playbackStateBuilder.setState(state, mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0, 1.0f);
        mediaSession.setPlaybackState(playbackStateBuilder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        executorService.shutdown();
    }
}
