package com.example.musicapp.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;

import com.example.musicapp.View.MusicFragment;
import com.example.musicapp.View.AlbumFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter{
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final  ArrayList<String> fragmentTitle = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    // return Fragment tương ứng với position tab
    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragmentArrayList.get(position);
    }

    // return số lượng tab
    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }


    public void addFragment(Fragment fragment, String title) {
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    // return title của tab tại vị trí cụ thể
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}

