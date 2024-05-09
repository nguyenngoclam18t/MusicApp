package com.example.musicapp.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.Controller.NewsTodayAdapter;
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
    RecyclerView.Adapter adapterNewsToday;

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

    private void SetEffect(){
        recyclerViewNewsToday.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        ArrayList<Singer> arr=new ArrayList<>();
        arr.add(new Singer("sơn tùng1",R.drawable.img_news_today));
        arr.add(new Singer("sơn tùng2",R.drawable.img_news_today));
        arr.add(new Singer("sơn tùng3",R.drawable.img_news_today));
        arr.add(new Singer("sơn tùng4",R.drawable.img_news_today));
        arr.add(new Singer("sơn tùng5",R.drawable.img_news_today));
        arr.add(new Singer("sơn tùng6",R.drawable.img_news_today));
        arr.add(new Singer("sơn tùng7",R.drawable.img_news_today));
        adapterNewsToday = new NewsTodayAdapter(arr);
        recyclerViewNewsToday.setAdapter(adapterNewsToday);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        recyclerViewNewsToday=(RecyclerView) view.findViewById(R.id.RecyclerViewNewsToday);
        SetEffect();
        return view;
    }
}