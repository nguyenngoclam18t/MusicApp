package com.example.musicapp.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.Controller.TopMusicHomePageAdapter;
import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopSongFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerViewTopSongFragment;

    public TopSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopSongFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        RecyclerView.Adapter adapterTop;
        ArrayList<SongModel> arrTop = new ArrayList<>(FireStoreDB.arrSong.subList(0, Math.min(FireStoreDB.arrSong.size(), 10)));
        adapterTop=new TopMusicHomePageAdapter(arrTop);
        recyclerViewTopSongFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTopSongFragment.setAdapter(adapterTop);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_top_song, container, false);
        recyclerViewTopSongFragment = (RecyclerView) view.findViewById(R.id.RecyclerViewTopSongFragment);
        // Inflate the layout for this fragment
        getTopToRecycleView();
        return view;
    }
}