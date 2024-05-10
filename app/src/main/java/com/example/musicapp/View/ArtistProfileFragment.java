package com.example.musicapp.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.musicapp.Controller.ViewPagerAdapter;
import com.example.musicapp.R;
import com.google.android.material.tabs.TabLayout;

public class ArtistProfileFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btnFollow;
    ImageButton btnBack;
    ImageView imgArtist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_profile, container, false);
        addControl(view);
        addEvents();
        return view;
    }

    public void addControl(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        btnFollow = view.findViewById(R.id.btnFollowArtist);
        btnBack = view.findViewById(R.id.btn_back);
        imgArtist = view.findViewById(R.id.artist_img);
    }

    public void addEvents() {
        tabLayout.setupWithViewPager(viewPager);

        // Khởi tạo adapter và thêm các fragment vào ViewPager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(new MusicFragment(), "Music");
        viewPagerAdapter.addFragment(new AlbumFragment(), "Album");
        viewPager.setAdapter(viewPagerAdapter);
    }
}
