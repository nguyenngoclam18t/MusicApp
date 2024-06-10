package com.example.musicapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.example.musicapp.Model.ApiCollectionHomePage;
import com.example.musicapp.Model.DataProfilePage;
import com.example.musicapp.Model.FirebaseAuthencation;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadingScreen extends AppCompatActivity {
    private ZingMp3Api zingMp3Api;
    private ProgressBar progressBar;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        progressBar = findViewById(R.id.progressBar);
        zingMp3Api = new ZingMp3Api();

        executorService.execute(() -> {
            try {
                DataProfilePage.userModel.setUserUid(FirebaseAuthencation.mAuth.getUid());
                if( !DataProfilePage.userModel.getUserUid().equals("DQLhmdBam4hOn7PrNJAnvQ2QfuK2")){
                    GetInforUser();

                    JsonObject songData = zingMp3Api.getHome();
                    JsonObject itemsarr = songData.getAsJsonObject("data");
                    JsonArray data = itemsarr.getAsJsonArray("items");
                    getBanner((JsonObject) data.get(0));
                    getSong((JsonObject) data.get(2));
                    getPlayList((JsonObject) data.get(3), ApiCollectionHomePage.arrPlaylistSummer);
                    getPlayList((JsonObject) data.get(4), ApiCollectionHomePage.arrPlaylistChill);
                    getPlayList((JsonObject) data.get(5), ApiCollectionHomePage.arrPlaylistRemix);
                    getPlayList((JsonObject) data.get(10), ApiCollectionHomePage.arrPlaylistHot);
                }
                mainHandler.postDelayed(this::redirectHomePage,5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getBanner(@NonNull JsonObject data) {
        JsonArray arr = data.getAsJsonArray("items");
        for (JsonElement element : arr) {
            JsonObject itemObj = element.getAsJsonObject();
            String banner = itemObj.get("banner").getAsString();
            ApiCollectionHomePage.arrBanner.add(banner);
        }
    }
    public void getSong(@NonNull JsonObject data) {
        JsonObject item = data.getAsJsonObject("items");
        JsonArray arr = item.getAsJsonArray("all");
        for (JsonElement element : arr) {
            JsonObject itemObj = element.getAsJsonObject();
            String encodeId = itemObj.get("encodeId").getAsString();
            String title = itemObj.get("title").getAsString();
            String thumbnailM = itemObj.get("thumbnailM").getAsString();
            String artistsNames = itemObj.get("artistsNames").getAsString();
            long timestamp = itemObj.get("releaseDate").getAsLong();
            Date date = new Date(timestamp * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = sdf.format(date);
            ApiCollectionHomePage.arrSongNewRelease.add(new SongModel(encodeId, title, artistsNames, dateString, thumbnailM));
        }
    }

    public void getPlayList(@NonNull JsonObject data, ArrayList<PlaylistModel> modelArrayList) {
        JsonArray arr = data.getAsJsonArray("items");
        for (JsonElement element : arr) {
            JsonObject itemObj = element.getAsJsonObject();
            String encodeId = itemObj.get("encodeId").getAsString();
            String title = itemObj.get("title").getAsString();
            String thumbnailM = itemObj.get("thumbnailM").getAsString();
            String sortDescription = itemObj.get("sortDescription").getAsString();
            modelArrayList.add(new PlaylistModel(encodeId, title, thumbnailM, "", sortDescription));
        }
    }

    public void redirectHomePage() {
        Intent intent;
        if( DataProfilePage.userModel.getUserUid().equals("DQLhmdBam4hOn7PrNJAnvQ2QfuK2")){
            intent= new Intent(LoadingScreen.this, AdminLayout.class);
        }else {
            preloadImages();
            intent = new Intent(LoadingScreen.this, Nav_Bar_Menu.class);
        }
        startActivity(intent);
        finish();
    }

    private void preloadImages() {
        for (PlaylistModel model : ApiCollectionHomePage.arrPlaylistSummer) {
            loadImage(model.getThumbnailLm());
        }
        for (PlaylistModel model : ApiCollectionHomePage.arrPlaylistChill) {
            loadImage(model.getThumbnailLm());
        }
        for (PlaylistModel model : ApiCollectionHomePage.arrPlaylistHot) {
            loadImage(model.getThumbnailLm());
        }
        for (PlaylistModel model : ApiCollectionHomePage.arrPlaylistRemix) {
            loadImage(model.getThumbnailLm());
        }
        for (SongModel model : ApiCollectionHomePage.arrSongNewRelease) {
            loadImage(model.getThumbnailLm());
        }
        for (String model : ApiCollectionHomePage.arrBanner) {
            loadImage(model);
        }
    }

    private void loadImage(String imageUrls) {
        Picasso.get().load(imageUrls).fetch();
    }
    void GetInforUser(){

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            synchronized (DataProfilePage.userModel) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String userId=document.getId();
                                    if(userId.equals(DataProfilePage.userModel.getUserUid())){
                                        DataProfilePage.userModel.setAvatarUrl(document.getString("avatarUrl"));
                                        DataProfilePage.userModel.setEmail( document.getString("email"));
                                        DataProfilePage.userModel.setFullName( document.getString("fullName"));
                                        DataProfilePage.userModel.setMobile(document.getString("mobile"));
                                    }
                                    GetFavoriteList();
                                }

                            }

                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    void fetchDataAlbum(){

        executorService.execute(() -> {
            try {
                for (PlaylistModel model : DataProfilePage.arrFavoritePlaylist) {
                    ZingMp3Api zingMp3Api = new ZingMp3Api();
                    JsonObject data = zingMp3Api.getDetailPlaylist(model.getPlaylistId());
                    JsonObject itemObj = data.getAsJsonObject("data");
                    model.setPlaylistName(itemObj.get("title").getAsString());
                    model.setThumbnailLm(itemObj.get("thumbnailM").getAsString());
                    model.setSortDescription(itemObj.get("sortDescription").getAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    void GetFavoriteList(){
        db.collection("FarvoritePlayList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            synchronized (DataProfilePage.arrFavoritePlaylist) {
                                DataProfilePage.arrFavoritePlaylist = new ArrayList<>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String uid=document.getString("UserId");
                                    if(uid.equals(DataProfilePage.userModel.getUserUid())){
                                        String PlaylistID=document.getString("PlaylistID");
                                        PlaylistModel model=new PlaylistModel();
                                        model.setPlaylistId(PlaylistID);
                                        DataProfilePage.arrFavoritePlaylist.add(model);
                                    }
                                }
                                fetchDataAlbum();


                            }

                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
