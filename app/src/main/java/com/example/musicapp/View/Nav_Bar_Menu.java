package com.example.musicapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.musicapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Nav_Bar_Menu extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout framefragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_menu);
        //loadFragment(new HomePageFragment());
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
                    //loadFragment(new HomePageFragment());
                    // Load Shop fragment or perform other actions
                    return true;
                } else if (id == R.id.navigation_top) {
                    //loadFragment(new HomePageFragment());

                    // Load Top fragment or perform other actions
                    return true;
                } else if (id == R.id.navigation_genres) {
                    //loadFragment(new HomePageFragment());

                    // Load Cart fragment or perform other actions
                    return true;
                } else if (id == R.id.navigation_Profile) {
                    //loadFragment(new HomePageFragment());

                    // Load Profile fragment or perform other actions
                    return true;
                }
                return false;
            }
        });
    }
}