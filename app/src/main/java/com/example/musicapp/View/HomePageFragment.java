package com.example.musicapp.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.musicapp.Model.Music;
import com.example.musicapp.Model.Singer;
import com.example.musicapp.R;

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
    ArrayList<Music> arrMusic = new ArrayList<>();
    ArrayList<Singer> arrSinger = new ArrayList<>();



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
        arrSinger.add(new Singer("sơn tùng1","đây là 1 chàng ca sĩ trẻ đầy tài năng và tham vọng", R.drawable.img_news_today));
        arrSinger.add(new Singer("sơn tùng2","đây là 1 chàng ca sĩ trẻ đầy tài năng và tham vọng", R.drawable.img_news_today));
        arrSinger.add(new Singer("sơn tùng3","đây là 1 chàng ca sĩ trẻ đầy tài năng và tham vọng", R.drawable.img_news_today));
        arrSinger.add(new Singer("sơn tùng4","đây là 1 chàng ca sĩ trẻ đầy tài năng và tham vọng", R.drawable.img_news_today));
        arrSinger.add(new Singer("sơn tùng5","đây là 1 chàng ca sĩ trẻ đầy tài năng và tham vọng", R.drawable.img_news_today));
        arrSinger.add(new Singer("sơn tùng6","đây là 1 chàng ca sĩ trẻ đầy tài năng và tham vọng", R.drawable.img_news_today));
        arrSinger.add(new Singer("sơn tùng7","đây là 1 chàng ca sĩ trẻ đầy tài năng và tham vọng", R.drawable.img_news_today));
        RecyclerView.Adapter adapterNewsToday;
        adapterNewsToday = new NewsTodayAdapter(arrSinger);
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
        arrMusic.add(new Music("Chúng ta của tương lai", "Sơn Tùng", R.drawable.sample_img_musicnews2));
        arrMusic.add(new Music("THERE’S NO ONE AT ALL", "Sơn Tùng", R.drawable.sample_img_musicnews1));
        arrMusic.add(new Music("Chúng ta của tương lai", "Sơn Tùng", R.drawable.sample_img_musicnews2));
        arrMusic.add(new Music("THERE’S NO ONE AT ALL", "Sơn Tùng", R.drawable.sample_img_musicnews1));
        arrMusic.add(new Music("Chúng ta của tương lai", "Sơn Tùng", R.drawable.sample_img_musicnews2));
        arrMusic.add(new Music("THERE’S NO ONE AT ALL", "Sơn Tùng", R.drawable.sample_img_musicnews1));
        arrMusic.add(new Music("Chúng ta của tương lai", "Sơn Tùng", R.drawable.sample_img_musicnews2));
        arrMusic.add(new Music("THERE’S NO ONE AT ALL", "Sơn Tùng", R.drawable.sample_img_musicnews1));
        arrMusic.add(new Music("Chúng ta của tương lai", "Sơn Tùng", R.drawable.sample_img_musicnews2));
        arrMusic.add(new Music("THERE’S NO ONE AT ALL", "Sơn Tùng", R.drawable.sample_img_musicnews1));
        arrMusic.add(new Music("Chúng ta của tương lai", "Sơn Tùng", R.drawable.sample_img_musicnews2));
        arrMusic.add(new Music("THERE’S NO ONE AT ALL", "Sơn Tùng", R.drawable.sample_img_musicnews1));
        arrMusic.add(new Music("Chúng ta của tương lai", "Sơn Tùng", R.drawable.sample_img_musicnews2));
        arrMusic.add(new Music("THERE’S NO ONE AT ALL", "Sơn Tùng", R.drawable.sample_img_musicnews1));
        RecyclerView.Adapter adapterNewsMusic=new NewsMusicAdapter(arrMusic);
        recyclerViewNewsMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNewsMusic.setAdapter(adapterNewsMusic);

    }
    private void effectSuggestSinger(View view){
        ImageView img;
        TextView nameSinger,descSinger;
        img=(ImageView) view.findViewById(R.id.imgSuggestSingerHomepage);
        nameSinger=(TextView) view.findViewById(R.id.nameSuggestSingerHomepage);
        descSinger=(TextView) view.findViewById(R.id.descSuggestSingerHomepage);
        img.setImageResource(R.drawable.img_news_today);
        nameSinger.setText(arrSinger.get(0).getName());
        descSinger.setText(arrSinger.get(0).getAbout());
    }
    private  void effectTopHomePage(){
        RecyclerView.Adapter adapterTop;
        ArrayList<Music> arrTop = new ArrayList<>(arrMusic.subList(0, Math.min(arrMusic.size(), 5)));
        adapterTop=new TopMusicHomePageAdapter(arrTop);
        recyclerViewTopMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTopMusic.setAdapter(adapterTop);
    }
    private  void effectFavoriteSinger(){
        RecyclerView.Adapter adapterFavoriteSinger;
        adapterFavoriteSinger=new FarvoriteSingerAdapter(arrSinger);
        recyclerViewFavoriteSinger.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFavoriteSinger.setAdapter(adapterFavoriteSinger);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        imageSlider = (ImageSlider) view.findViewById(R.id.imgSliderHomePage);
        recyclerViewNewsToday = (RecyclerView) view.findViewById(R.id.RecyclerViewNewsToday);
        recyclerViewNewsMusic = (RecyclerView) view.findViewById(R.id.RecyclerViewNewsMusic);
        recyclerViewTopMusic = (RecyclerView) view.findViewById(R.id.RecyclerViewTopMusic);
        recyclerViewFavoriteSinger=(RecyclerView) view.findViewById(R.id.RecyclerViewFarvoriteSinger);
        effectSlider();
        effectNewsToday();
        effectSuggestSinger(view);
        effectNewsMusic();
        effectTopHomePage();
        effectFavoriteSinger();
        return view;
    }
}