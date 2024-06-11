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
import android.content.ComponentName;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.support.v4.media.session.MediaSessionCompat;

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
    private ZingMp3Api zingMp3Api;
    private ExecutorService executorService;
    private SongModel currentSong;
    private MediaSessionCompat mediaSession;
    private int currentSongIndex = 0;
    public static PlayerActivity instance;

    private static final String TAG = "PlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        handler = new Handler();
        zingMp3Api = new ZingMp3Api();
        executorService = Executors.newSingleThreadExecutor();
        Intent intent = getIntent();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_ID);
        }

        if (intent != null && intent.hasExtra("encodeId")) {
            String encodeId = intent.getStringExtra("encodeId");
            String title = intent.getStringExtra("title");
            String thumbnail = intent.getStringExtra("thumbnailM");
            String artistsNames = intent.getStringExtra("artistsNames");
            String songUrl = intent.getStringExtra("link");
            int duration = intent.getIntExtra("duration", 0);

            currentSong = new SongModel(encodeId, title, artistsNames, thumbnail, songUrl, duration);

            songList.add(currentSong);

            updateUI(currentSong);

            loadSongUrl(encodeId);

        } else {
            Toast.makeText(this, "Không có bài hát được chọn", Toast.LENGTH_SHORT).show();
            finish();
        }

        addSongToList(currentSong);
    }

    private void addSongToList(SongModel song) {
        if (!songList.contains(song)) {
            getListSong(50);
            songList.add(song);
        }
    }

    private void initViews() {
        songImage = findViewById(R.id.song_img);
        songTitle = findViewById(R.id.song_title);
        artistName = findViewById(R.id.artist_name);
        durationPlayed = findViewById(R.id.drationPlayed);
        durationTotal = findViewById(R.id.drationTotal);
        seekBar = findViewById(R.id.seekbar);
        btnPlay = findViewById(R.id.play_pause);
        btnRepeat = findViewById(R.id.btn_repeat);
        btnShuffle = findViewById(R.id.btn_shuffle);
        btnFav = findViewById(R.id.btnFav);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);

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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress * 1000);
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

    public void play() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.pause);
        }
    }

    public void getSong(@NonNull JsonObject data) {
        if (songList == null) {
            songList = new ArrayList<>();
        }
        if (data.has("items")) {
            JsonArray itemsArray = data.getAsJsonArray("items");
            for (JsonElement itemElement : itemsArray) {
                JsonObject item = itemElement.getAsJsonObject();
                if (item.has("items")) {
                    JsonArray itemsObj = item.getAsJsonArray("items");
                    if (itemsObj.size() > 0) {
                        for (JsonElement element : itemsObj) {
                            JsonObject itemObj = element.getAsJsonObject();
                            boolean isWorldWide = itemObj.get("isWorldWide").getAsBoolean();
                            if (isWorldWide) {
                                String encodeId = itemObj.get("encodeId").getAsString();
                                String title = itemObj.get("title").getAsString();
                                String thumbnailM = itemObj.get("thumbnailM").getAsString();
                                String artistsNames = itemObj.get("artistsNames").getAsString();
                                String songUrl = itemObj.get("link").getAsString();
                                int duration = itemObj.get("duration").getAsInt();
                                SongModel song = new SongModel(encodeId, title, artistsNames, thumbnailM, songUrl, duration);
                                songList.add(song);
                            }
                        }
                    }
                }
            }
        }
    }




    public void getListSong(int limit) {
        executorService.execute(() -> {
            try {
                JsonObject songData = zingMp3Api.getHome();
                Log.d(TAG, "Dữ liệu bài hát: " + songData.toString());
                if (songData != null) {
                    JsonObject itemsObj = songData.getAsJsonObject("data");
                    if (itemsObj != null && itemsObj.has("items")) {
                        JsonArray itemsArray = itemsObj.getAsJsonArray("items");
                        for (JsonElement itemElement : itemsArray) {
                            JsonObject item = itemElement.getAsJsonObject();
                            if (item.has("items")) {
                                JsonArray itemsData = item.getAsJsonArray("items");
                                for (JsonElement element : itemsData) {
                                    JsonObject itemObj = element.getAsJsonObject();
                                    if (itemObj.has("isWorldWide")) {
                                        boolean isWorldWide = itemObj.get("isWorldWide").getAsBoolean();
                                        if (isWorldWide) {
                                            getListSong(50);
                                        }
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "Số lượng bài hát đã được thêm vào songList: " + songList.size());
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



    private void loadSongUrl(String encodeId) {
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
                        showErrorAndFinish("Không tìm thấy url");
                    }
                } else {
                    showErrorAndFinish("Không có dữ liệu");
                }
            } catch (Exception e) {
                Log.e(TAG, "Lỗi: ", e);
                showErrorAndFinish("Lỗi");
            }
        });
    }




    private void showErrorAndFinish(String message) {
        runOnUiThread(() -> {
            Toast.makeText(PlayerActivity.this, message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public void playSong(String songUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                btnPlay.setImageResource(R.drawable.pause);
                seekBar.setMax(mp.getDuration() / 1000);
                updateSeekBar();
                createNotification();
                updateNotificationPlaybackState(PlaybackStateCompat.STATE_PLAYING);
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                if (isRepeat) {
                    playSong(songUrl);
                } else {
                    playNextSong();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "Lỗi: ", e);
            showErrorAndFinish("Lỗi");
        }
    }


    private void stopCurrentSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playNextSong() {
        if (isShuffle) {
            playRandomSong();
        } else {
            currentSongIndex = (currentSongIndex + 1) % songList.size();
            currentSong = songList.get(currentSongIndex);
            updateUI(currentSong);
            loadSongUrl(currentSong.getSongId());
        }
    }


    public void playPreviousSong() {
        currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
        currentSong = songList.get(currentSongIndex);
        updateUI(currentSong);
        loadSongUrl(currentSong.getSongId());
    }



    private void playRandomSong() {
        Random random = new Random();
        currentSongIndex = random.nextInt(songList.size());
        currentSong = songList.get(currentSongIndex);
        updateUI(currentSong);
        loadSongUrl(currentSong.getSongId());
    }

    private void updateSeekBar() {
        handler.postDelayed(updateTimeTask, 1000);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            if (mediaPlayer != null) {
                long totalDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();

                durationTotal.setText(getDuration(totalDuration));
                durationPlayed.setText(getDuration(currentDuration));

                int progress = (int) (currentDuration / 1000);
                seekBar.setProgress(progress);

                handler.postDelayed(this, 1000);
            }
        }
    };

    private String getDuration(long duration) {
        long seconds = duration / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updateUI(SongModel song) {
        if (song != null) {
            currentSong = song;
            songTitle.setText(song.getTitle());
            artistName.setText(song.getArtistsNames());
            Picasso.get().load(song.getThumbnailLm()).into(songImage);
            updateNotification(song.getTitle(), song.getArtistsNames());
        }
    }

    private void addSongToFavorites(String songId) {
        if (DataProfilePage.userModel.getFavoritePlaylists().contains(songId)) {
            Toast.makeText(PlayerActivity.this, "Bài hát đã có trong danh sách yêu thích.", Toast.LENGTH_SHORT).show();
        } else {

            DataProfilePage.userModel.getFavoritePlaylists().add(songId);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(DataProfilePage.userModel.getUserUid())
                    .update("favoritePlaylists", DataProfilePage.userModel.getFavoritePlaylists())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PlayerActivity.this, "Đã thêm vào danh sách yêu thích.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PlayerActivity.this, "Thêm vào danh sách yêu thích thất bại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void initializeMediaSession() {
        ComponentName mediaButtonReceiver = new ComponentName(getApplicationContext(), MediaButtonReceiver.class);
        mediaSession= new MediaSessionCompat(getApplicationContext(), "PlayerActivity", mediaButtonReceiver, null);
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onCommand(String command, Bundle extras, ResultReceiver cb) {
                super.onCommand(command, extras, cb);
            }
        });
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}