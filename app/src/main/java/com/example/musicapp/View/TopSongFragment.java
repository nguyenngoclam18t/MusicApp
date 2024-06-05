package com.example.musicapp.View;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicapp.Controller.TopMusicHomePageAdapter;
import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopSongFragment extends Fragment implements OnSongClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerViewTopSongFragment;
    String ablumid;
    ImageView img;
    public TopSongFragment() {
        // Required empty public constructor
    }


    public static TopSongFragment newInstance(String param1, String param2) {
        TopSongFragment fragment = new TopSongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void getTopToRecycleView(){
        Bundle bundle = getArguments();

        ArrayList<SongModel> arrTop=new ArrayList<>();
        RecyclerView.Adapter adapterTop;
        if (bundle == null) {
            arrTop = new ArrayList<>(FireStoreDB.arrSong.subList(0, Math.min(FireStoreDB.arrSong.size(), 10)));
        } else {
            String albumId = bundle.getString("albumId");
            String albumimg = bundle.getString("albumimg");
            Picasso.get()
                    .load(albumimg)
                    .placeholder(R.drawable.todays_top_hits)
                    .into(img);
            for (SongModel song : FireStoreDB.arrSong) {
                if (song.getAlbumId() != null && song.getAlbumId().equals(albumId)) {
                    arrTop.add(song);
                }
            }
        }
        adapterTop=new TopMusicHomePageAdapter(arrTop,this);
        recyclerViewTopSongFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTopSongFragment.setAdapter(adapterTop);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_top_song, container, false);
        recyclerViewTopSongFragment = (RecyclerView) view.findViewById(R.id.RecyclerViewTopSongFragment);
        img=(ImageView)view.findViewById(R.id.imageBackground);
        // Inflate the layout for this fragment
        getTopToRecycleView();
        return view;
    }
    @Override
    public void onSongClick(SongModel song) {
        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra("songId", song.getSongId());
        startActivity(intent);
    }
}