package com.example.musicapp.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.musicapp.Controller.FarvoriteSingerAdapter;
import com.example.musicapp.Controller.NewsMusicAdapter;
import com.example.musicapp.Controller.NewsTodayAdapter;
import com.example.musicapp.Controller.TopMusicHomePageAdapter;
import com.example.musicapp.Model.ArtistsModel;
import com.example.musicapp.Model.SongModel;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //my varible
    RecyclerView recyclerViewNewsToday;
    RecyclerView recyclerViewNewsMusic;
    RecyclerView recyclerViewTopMusic;
    RecyclerView recyclerViewFavoriteSinger;

    ImageSlider imageSlider;
    ArrayList<SongModel> arrSong = new ArrayList<>();
    ArrayList<ArtistsModel> arrArtists = new ArrayList<>();

    FirebaseFirestore db;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
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

    private void effectNewsToday() {

        recyclerViewNewsToday.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.Adapter adapterNewsToday;
        adapterNewsToday = new NewsTodayAdapter(arrArtists);
        recyclerViewNewsToday.setAdapter(adapterNewsToday);

    }

    private void effectSlider() {
        // slider
        ArrayList<SlideModel> arrSlider = new ArrayList<>();
        arrSlider.add(new SlideModel(R.drawable.sample_slider1, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider2, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider1, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider2, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider1, ScaleTypes.CENTER_CROP));
        arrSlider.add(new SlideModel(R.drawable.sample_slider2, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(arrSlider, ScaleTypes.CENTER_CROP);
    }

    private void effectNewsMusic() {
        ArrayList<SongModel> arrNewsSongs = new ArrayList<>(arrSong.subList(0, Math.min(arrSong.size(), 15)));
        RecyclerView.Adapter adapterNewsMusic=new NewsMusicAdapter(arrNewsSongs);
        recyclerViewNewsMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNewsMusic.setAdapter(adapterNewsMusic);

    }
    private void effectSuggestSinger(View view){
        ImageView img;
        TextView nameSinger,descSinger;
        img=(ImageView) view.findViewById(R.id.imgSuggestSingerHomepage);
        nameSinger=(TextView) view.findViewById(R.id.nameSuggestSingerHomepage);
        descSinger=(TextView) view.findViewById(R.id.descSuggestSingerHomepage);
        Picasso.get()
                .load(arrArtists.get(0).avatarUrl)
                .into(img);
        nameSinger.setText(arrArtists.get(0).artistId);
        descSinger.setText("đây là 1 ca sĩ trẻ đầy  tài năng và mang đột phá trong gout âm nhạc của mình mong muốn cháy bỏng để phát triển bản thân.");
    }
    private  void effectTopHomePage(){
        RecyclerView.Adapter adapterTop;
        ArrayList<SongModel> arrTop = new ArrayList<>(arrSong.subList(0, Math.min(arrSong.size(), 5)));
        adapterTop=new TopMusicHomePageAdapter(arrTop);
        recyclerViewTopMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTopMusic.setAdapter(adapterTop);
    }
    private  void effectFavoriteSinger(){
        RecyclerView.Adapter adapterFavoriteSinger;
        adapterFavoriteSinger=new FarvoriteSingerAdapter(arrArtists);
        recyclerViewFavoriteSinger.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFavoriteSinger.setAdapter(adapterFavoriteSinger);
    }
    private void getArray(View view){
        db.collection("songs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String imgUrl = document.getString("imgUrl");
                                String genreId = document.getString("genreId");
                                String albumId = document.getString("albumId");
                                String artistId = document.getString("artistId");
                                String title = document.getString("title");
                                String songUrl = document.getString("songUrl");
                                arrSong.add(new SongModel(document.getId().toString(),title,artistId,albumId,genreId,imgUrl,songUrl));
                            }
                            effectNewsMusic();
                            effectTopHomePage();
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("artists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String avartaUrl = document.getString("avatarUrl");
                                arrArtists.add(new ArtistsModel(document.getId().toString(),avartaUrl));
                            }
                            effectSuggestSinger(view);
                            effectFavoriteSinger();
                            effectNewsToday();
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseApp.initializeApp(getContext());
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        imageSlider = (ImageSlider) view.findViewById(R.id.imgSliderHomePage);
        recyclerViewNewsToday = (RecyclerView) view.findViewById(R.id.RecyclerViewNewsToday);
        recyclerViewNewsMusic = (RecyclerView) view.findViewById(R.id.RecyclerViewNewsMusic);
        recyclerViewTopMusic = (RecyclerView) view.findViewById(R.id.RecyclerViewTopMusic);
        recyclerViewFavoriteSinger=(RecyclerView) view.findViewById(R.id.RecyclerViewFarvoriteSinger);
        getArray(view);
        effectSlider();


        return view;
    }
}