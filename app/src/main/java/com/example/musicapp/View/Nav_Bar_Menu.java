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
                    loadFragment(new ProfileFragment());

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


}
