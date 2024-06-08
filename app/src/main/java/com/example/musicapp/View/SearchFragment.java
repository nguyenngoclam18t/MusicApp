package com.example.musicapp.View;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
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

import com.example.musicapp.Controller.AlbumHomePageAdapter;
import com.example.musicapp.Controller.FarvoriteSingerAdapter;
import com.example.musicapp.Controller.TopMusicHomePageAdapter;
import com.example.musicapp.Model.ApiCollectionHomePage;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.OnArtistClick;
import com.example.musicapp.Model.OnSongClick;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
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
    ZingMp3Api zingMp3Api = new ZingMp3Api();
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
    public void setAlbum( ArrayList<PlaylistModel> arr){
        rclAlbum.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.Adapter adapterNewsToday;
        if (arr.size()>0){
            txtAlbum.setText("Album");
        }
        adapterNewsToday = new AlbumHomePageAdapter(arr,this);
        rclAlbum.setAdapter(adapterNewsToday);
    }

    public void setSong(ArrayList<SongModel> arr){
        RecyclerView.Adapter adapterTop;
        if (arr.size()>0){
            txtSong.setText("Song");
        }
        adapterTop=new TopMusicHomePageAdapter(arr,this);
        rclSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rclSong.setAdapter(adapterTop);
    }
    public void setArtists(ArrayList<ArtistsModel> arr){
        RecyclerView.Adapter adapterFavoriteSinger;
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

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchString = editText.getText().toString().trim();
                    txtArtists.setText("");
                    txtSong.setText("");
                    txtAlbum.setText("");

                    new Thread(() -> {
                        try {
                            JsonObject songData = zingMp3Api.search(searchString);
                            JsonObject itemsarr=songData.getAsJsonObject("data");
                            JsonArray dataArtist=itemsarr.getAsJsonArray("artists");
                            JsonArray dataSong=itemsarr.getAsJsonArray("songs");
                            JsonArray dataAlbum=itemsarr.getAsJsonArray("playlists");
                            ArrayList<ArtistsModel>artistsModelArrayList=new ArrayList<>();
                            ArrayList<PlaylistModel>playlistModelArrayList=new ArrayList<>();
                            ArrayList<SongModel>songModelArrayList=new ArrayList<>();
                            for (JsonElement element: dataArtist) {
                                JsonObject items=element.getAsJsonObject();
                                String artistId = items.get("id").getAsString();
                                String artistName = items.get("name").getAsString();
                                String artistAliasName = items.get("alias").getAsString();
                                String sortBiography = "";
                                String thumbnailLm = items.get("thumbnailM").getAsString();
                                artistsModelArrayList.add(new ArtistsModel(artistId, artistName, artistAliasName, sortBiography, thumbnailLm));
                            }
                            for (JsonElement element :dataSong){
                                JsonObject itemObj = element.getAsJsonObject();
                                String encodeId= itemObj.get("encodeId").getAsString();
                                String title= itemObj.get("title").getAsString();
                                String thumbnailM= itemObj.get("thumbnailM").getAsString();
                                String artistsNames= itemObj.get("artistsNames").getAsString();
                                long timestamp = itemObj.get("releaseDate").getAsLong();
                                Date date = new Date(timestamp * 1000);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                String dateString = sdf.format(date);
                                songModelArrayList.add(new SongModel(encodeId,title,artistsNames,dateString,thumbnailM));
                            }
                            for (JsonElement element :dataAlbum){
                                JsonObject itemObj = element.getAsJsonObject();
                                String encodeId= itemObj.get("encodeId").getAsString();
                                String title= itemObj.get("title").getAsString();
                                String thumbnailM= itemObj.get("thumbnailM").getAsString();
                                String sortDescription= itemObj.get("sortDescription").getAsString();
                                playlistModelArrayList.add(new PlaylistModel(encodeId,title,thumbnailM,"",sortDescription));
                            }
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    setArtists(artistsModelArrayList);
                                    setAlbum(playlistModelArrayList);
                                    setSong(songModelArrayList);
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                    ;
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
    public void OnAlbumClick(PlaylistModel album) {
        Bundle bundle = new Bundle();
        bundle.putString("albumId", album.getPlaylistId());
        TopSongFragment topSongFragment=new TopSongFragment();
        topSongFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, topSongFragment).addToBackStack(null).commit();
    }
    public void onArtistClick(ArtistsModel artists) {
        Bundle bundle = new Bundle();
        bundle.putString("artistId", artists.getArtistId());
        ArtistProfileFragment artistProfileFragment = new ArtistProfileFragment();
        artistProfileFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, artistProfileFragment).addToBackStack(null).commit();
    }
}