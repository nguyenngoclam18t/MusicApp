package com.example.musicapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.musicapp.Model.DataProfilePage;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Nav_Bar_Menu extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout framefragment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_menu);
        loadFragment(new HomePageFragment());
        FirebaseApp.initializeApp(this);
        //checkout
        Get();

    }

    void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.FrameHomePage, fragment);
        ft.commit();
    }

    void Get() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navBottomBarMenu);
        framefragment = (FrameLayout) findViewById(R.id.FrameHomePage);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_homepage) {
                    loadFragment(new HomePageFragment());
                    // Load Shop fragment or perform other actions
                    return true;
                } else if (id == R.id.navigation_top) {
                    loadFragment(new TopSongFragment());

                    // Load Top fragment or perform other actions
                    return true;
                }  else if (id == R.id.navigation_Profile) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword("nguyenngoclam2k3d@gmail.com", "nguyenngoclam")
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    handleAfterLogin(user);
                                    GetInforUser();

                                } else {
                                    Log.d("test", "onNavigationItemSelected: ");
                                }
                            });



                    // Load Profile fragment or perform other actions
                    return true;
                }  else if (id == R.id.navigation_Search) {
                    loadFragment(new SearchFragment());

                // Load Profile fragment or perform other actions
                return true;
            }
                return false;
            }
        });
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
                                }
                                GetFavoriteList();
                            }

                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    void fetchDataAlbum(){

        new Thread(() -> {
            try {
                for (PlaylistModel model:DataProfilePage.arrFavoritePlaylist) {
                    ZingMp3Api zingMp3Api=new ZingMp3Api();
                    JsonObject data = zingMp3Api.getDetailPlaylist(model.getPlaylistId());
                    JsonObject itemObj=data.getAsJsonObject("data");
                    model.setPlaylistName(itemObj.get("title").getAsString());
                    model.setThumbnailLm( itemObj.get("thumbnailM").getAsString());
                    model.setSortDescription( itemObj.get("sortDescription").getAsString());
                }
                runOnUiThread(() -> loadFragment(new ProfileFragment()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


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
    private void handleAfterLogin(FirebaseUser user) {
        if (user != null) {
            DataProfilePage.userModel.setUserUid( user.getUid());

        }
    }
}
