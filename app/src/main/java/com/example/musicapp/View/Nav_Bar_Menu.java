package com.example.musicapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.musicapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Nav_Bar_Menu extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_menu);
        loadFragment(new HomePageFragment());

        bottomNavigationView = findViewById(R.id.navBottomBarMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.navigation_homepage) {
                    fragment = new HomePageFragment();
                } else if (item.getItemId() == R.id.navigation_top) {
                    fragment = new TopSongFragment();
                } else if (item.getItemId() == R.id.navigation_Profile) {
                    fragment = new ProfileFragment();
                } else if (item.getItemId() == R.id.navigation_Search) {
                    fragment = new SearchFragment();
                }

                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment == null)
            return false;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameHomePage, fragment)
                .commit();
        return true;
    }
}
