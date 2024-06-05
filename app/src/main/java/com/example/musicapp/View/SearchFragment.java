package com.example.musicapp.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.musicapp.Controller.FarvoriteSingerAdapter;
import com.example.musicapp.Controller.NewsTodayAdapter;
import com.example.musicapp.Controller.TopMusicHomePageAdapter;
import com.example.musicapp.Model.AlbumModel;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.FireStoreDB;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.OnArtistClick;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements OnArtistClick, OnSongClick, OnAlbumClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputEditText editText ;
    TextView txtArtists,txtSong,txtAlbum;
    String searchString="";
    RecyclerView rclSong,rclArtists,rclAlbum;
    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public void setAlbum(){
        rclAlbum.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.Adapter adapterNewsToday;
        ArrayList<AlbumModel> arr=new ArrayList<>();
        for (AlbumModel albumModel:FireStoreDB.arrAlbum) {
            if (albumModel.albumName.toLowerCase(Locale.ROOT).contains(searchString.toLowerCase())){
                arr.add(albumModel);
            }
        }
        if (arr.size()>0){
            txtAlbum.setText("Album");
        }
        adapterNewsToday = new NewsTodayAdapter(arr,this);
        rclAlbum.setAdapter(adapterNewsToday);
    }
    public void setSong(){
        RecyclerView.Adapter adapterTop;
        ArrayList<SongModel> arr=new ArrayList<>();
        for (SongModel songModel:FireStoreDB.arrSong) {
            if (songModel.getTitle().toLowerCase(Locale.ROOT).contains(searchString.toLowerCase())){
                arr.add(songModel);
            }
        }
        if (arr.size()>0){
            txtSong.setText("Song");
        }
        adapterTop=new TopMusicHomePageAdapter(arr,this);
        rclSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rclSong.setAdapter(adapterTop);
    }
    public void setSong(String GenreName){
        RecyclerView.Adapter adapterTop;
        ArrayList<SongModel> arr=new ArrayList<>();
        for (SongModel songModel:FireStoreDB.arrSong) {
            if (songModel.getGenreId().toLowerCase(Locale.ROOT).contains(GenreName.toLowerCase())){
                arr.add(songModel);
            }
        }
        if (arr.size()>0){
            txtSong.setText("Song");
        }
        adapterTop=new TopMusicHomePageAdapter(arr,this);
        rclSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rclSong.setAdapter(adapterTop);
    }
    public void setArtists(){
        RecyclerView.Adapter adapterFavoriteSinger;
        ArrayList<ArtistsModel> arr=new ArrayList<>();
        for (ArtistsModel artistsModel:FireStoreDB.arrArtists) {
            if (artistsModel.getArtistName().toLowerCase(Locale.ROOT).contains(searchString.toLowerCase())){
                arr.add(artistsModel);
            }
        }
        if (arr.size()>0){
            txtArtists.setText("Artists");
        }
        adapterFavoriteSinger=new FarvoriteSingerAdapter(arr, this);
        rclArtists.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rclArtists.setAdapter(adapterFavoriteSinger);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        editText=(TextInputEditText) view.findViewById(R.id.EdtSearchString);
        rclAlbum=(RecyclerView)view.findViewById(R.id.SearchRecycleViewAlbum);
        rclArtists=(RecyclerView)view.findViewById(R.id.SearchRecycleViewSinger);
        rclSong=(RecyclerView)view.findViewById(R.id.SearchRecycleViewSong);
        txtSong=(TextView) view.findViewById(R.id.txtSearchSong);
        txtAlbum=(TextView) view.findViewById(R.id.txtSearchAlbum);
        txtArtists=(TextView) view.findViewById(R.id.txtSearchArtists);
        Bundle bundle = getArguments();
        if(bundle!=null){
            searchString = bundle.getString("GenreName");
            txtArtists.setText("");
            txtSong.setText("");
            txtAlbum.setText("");
            setAlbum();
            setSong(searchString);
            setArtists();
        }
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                     searchString = editText.getText().toString().trim();
                    txtArtists.setText("");
                    txtSong.setText("");
                    txtAlbum.setText("");
                    setAlbum();
                    setSong();
                    setArtists();
                    return true;
                }
                return false;
            }
        });
        return view;
    }
    @Override
    public void onSongClick(SongModel song) {
        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra("songId", song.getSongId());
        startActivity(intent);
    }
    @Override
    public void OnAlbumClick(AlbumModel album) {
        Bundle bundle = new Bundle();
        bundle.putString("albumId", album.getAlbumId());
        bundle.putString("albumimg", album.getImageUrl());
        TopSongFragment topSongFragment=new TopSongFragment();
        topSongFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, topSongFragment).addToBackStack(null).commit();
    }
    public void onArtistClick(ArtistsModel artists) {
        Bundle bundle = new Bundle();
        bundle.putString("artistId", artists.getArtistId());
        bundle.putString("artistName", artists.getArtistName());
        bundle.putString("avatarUrl", artists.getAvatarUrl());
        ArtistProfileFragment artistProfileFragment = new ArtistProfileFragment();
        artistProfileFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, artistProfileFragment).addToBackStack(null).commit();
    }
}