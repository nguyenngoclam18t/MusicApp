package com.example.musicapp.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;

import com.example.musicapp.Controller.GenreSongAdapter;
import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenreSongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenreSongsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GridLayout gridGenre ;

    public GenreSongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenreSongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenreSongsFragment newInstance(String param1, String param2) {
        GenreSongsFragment fragment = new GenreSongsFragment();
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
    private void populateGridLayout(GenreSongAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            View genreView = adapter.getView(i, null, gridGenre);
            gridGenre.addView(genreView);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_genre_songs, container, false);
        gridGenre=(GridLayout) view.findViewById(R.id.GridlayoutGenreSongs);
        GenreSongAdapter adapter=new GenreSongAdapter(getContext(), FireStoreDB.arrGenre);
        populateGridLayout(adapter);
        // Inflate the layout for this fragment
        return view;
    }
}