package com.example.musicapp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.Controller.ViewPagerAdapter;
import com.example.musicapp.Model.AlbumModel;
import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.R;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ArtistProfileFragment extends Fragment implements OnAlbumClick {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton btnFollow;
    private ImageButton btnBack;
    private ImageView imgArtist;
    private TextView artistName;
    FireStoreDB fireStoreDB = new FireStoreDB();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_profile, container, false);
        addControl(view);
        setupTabLayoutAndViewPager();
        loadData();
        addEvents();
        return view;
    }

    private void addControl(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        btnFollow = view.findViewById(R.id.btnFollowArtist);
        btnBack = view.findViewById(R.id.btn_back);
        imgArtist = view.findViewById(R.id.artist_img);
        artistName = view.findViewById(R.id.artistName);
    }

    private void setupTabLayoutAndViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String artistId = bundle.getString("artistId");
            String artistNameStr = bundle.getString("artistName");
            String avatarUrl = bundle.getString("avatarUrl");

            if (artistNameStr != null) {
                artistName.setText(artistNameStr);
            }

            if (avatarUrl != null) {
                Picasso picasso = Picasso.get();
                picasso.load(avatarUrl).into(imgArtist);
            }

            Bundle musicBundle = new Bundle();
            musicBundle.putString("artistId", artistId);
            musicBundle.putString("artistName", artistNameStr);
            MusicFragment musicFragment = new MusicFragment();
            musicFragment.setArguments(musicBundle);

            Bundle albumBundle = new Bundle();
            albumBundle.putString("artistId", artistId);
            AlbumFragment albumFragment = new AlbumFragment(this);
            albumFragment.setArguments(albumBundle);

            ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
            if (viewPagerAdapter != null) {
                viewPagerAdapter.addFragment(musicFragment, "Music");
                viewPagerAdapter.addFragment(albumFragment, "Album");
                viewPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addEvents() {
        btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        btnFollow.setOnClickListener(v -> {
        });

    }

    public static RequestCreator resizeImage(RequestCreator requestCreator, int targetWidth, int targetHeight) {
        return requestCreator.resize(targetWidth, targetHeight).centerCrop();
    }
    @Override
    public void OnAlbumClick(AlbumModel album) {
        Bundle bundle = new Bundle();
        bundle.putString("albumId", album.getAlbumName());
        bundle.putString("albumimg", album.getImageUrl());
        TopSongFragment topSongFragment=new TopSongFragment();
        topSongFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, topSongFragment).addToBackStack(null).commit();
    }
}
