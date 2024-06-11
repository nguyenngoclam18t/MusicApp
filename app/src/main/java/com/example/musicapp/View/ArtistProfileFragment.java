package com.example.musicapp.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.musicapp.Controller.ViewPagerAdapter;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ArtistProfileFragment extends Fragment implements OnAlbumClick {

    private static final String TAG = "ArtistProfileFragment";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton btnBack;
    private ImageView imgArtist;
    private TextView artistName;
    private ZingMp3Api zingMp3Api;
    private ArtistsModel artist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_profile, container, false);
        zingMp3Api = new ZingMp3Api();
        addControl(view);
        setupTabLayoutAndViewPager(view);
        loadData();
        addEvents();
        return view;
    }

    private void addControl(View view) {
        btnBack = view.findViewById(R.id.btn_back);
        imgArtist = view.findViewById(R.id.artist_img);
        artistName = view.findViewById(R.id.artistName);
    }

    private void setupTabLayoutAndViewPager(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String artistId = bundle.getString("artistId");
            String artistNameStr = bundle.getString("artistName");
            String avatarUrl = bundle.getString("thumbnailLm");

            if (artistNameStr != null) {
                artistName.setText(artistNameStr);
            }

            if (avatarUrl != null) {
                Picasso.get().load(avatarUrl).into(imgArtist, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Image loaded successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Failed to load image: " + e.getMessage());
                    }
                });
            }

            if (artistId != null) {
                if (artist == null) {
                    getArtistInfo(artistId);
                }

                Bundle musicBundle = new Bundle();
                musicBundle.putString("artistId", artistId);
                musicBundle.putString("artistName", artistNameStr);
                MusicFragment musicFragment = new MusicFragment();
                musicFragment.setArguments(musicBundle);

                Bundle albumBundle = new Bundle();
                albumBundle.putString("artistId", artistId);
                albumBundle.putString("artistName", artistNameStr);
                AlbumFragment albumFragment = new AlbumFragment();
                albumFragment.setArguments(albumBundle);

                ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
                if (viewPagerAdapter != null) {
                    viewPagerAdapter.addFragment(musicFragment, "Âm nhạc");
                    viewPagerAdapter.addFragment(albumFragment, "Playlist");
                    viewPagerAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void getArtistInfo(String artistId) {
        new Thread(() -> {
            try {
                JsonObject artistDetail = zingMp3Api.getArtist(artistId);
                requireActivity().runOnUiThread(() -> {
                    if (artistDetail != null && artistDetail.has("data")) {
                        JsonObject artistData = artistDetail.getAsJsonObject("data");
                        updateArtistInfo(artistData);
                    } else {
                        Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> {
                });
            }
        }).start();
    }

    private void updateArtistInfo(JsonObject artistInfo) {
        requireActivity().runOnUiThread(() -> {
            String name = artistInfo.get("name").getAsString();
            String avatarUrl = artistInfo.get("thumbnailLm").getAsString();
            artist = new ArtistsModel();
            artist.setArtistName(name);
            artist.setThumbnailLm(avatarUrl);

            Picasso.get().load(artist.getThumbnailLm()).into(imgArtist, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Image loaded successfully");
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "Failed to load image: " + e.getMessage());
                }
            });
            artistName.setText(artist.getArtistName());
        });
    }

    private void addEvents() {
        btnBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    @Override
    public void OnAlbumClick(PlaylistModel album) {
        Bundle bundle = new Bundle();
        bundle.putString("playlistId", album.getPlaylistId());
        bundle.putString("playlistName", album.getPlaylistName());
        bundle.putString("ThumbnailM", album.getThumbnailLm());
        bundle.putString("sortDescription", album.getSortDescription());

        AlbumDetailFragment albumDetailFragment = new AlbumDetailFragment();
        albumDetailFragment.setArguments(bundle);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.FrameHomePage, albumDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}
