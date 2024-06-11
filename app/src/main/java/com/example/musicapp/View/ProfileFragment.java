package com.example.musicapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Controller.AlbumHomePageAdapter;
import com.example.musicapp.Model.DataProfilePage;
import com.example.musicapp.Model.FirebaseAuthencation;
import com.example.musicapp.Model.OnAlbumClick;
import com.example.musicapp.Model.PlaylistModel;
import com.example.musicapp.Model.UserModel;
import com.example.musicapp.Model.ZingMp3Api;
import com.example.musicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements OnAlbumClick {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String userUID="";
    private String mParam1;
    private String mParam2;
    ImageView avatarUser;
    TextView nameUser;
    Button updateInfor,btnUpdate,btnLogout;
    EditText fullname,email,number;
    LinearLayout layoutUpdate;

    RecyclerView recyclerViewFarvoritePlaylist;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    void Get(View view){
        avatarUser=(ImageView)view.findViewById(R.id.avatarUserProfile);
        nameUser=(TextView) view.findViewById(R.id.UserNameProfile);
        recyclerViewFarvoritePlaylist=(RecyclerView) view.findViewById(R.id.RecyclerViewFarvoritePlaylist);
        updateInfor=(Button) view.findViewById(R.id.btnRedirectUpdateFragment);
        btnLogout=(Button) view.findViewById(R.id.btnLogout);
        email=(EditText) view.findViewById(R.id.edtEmailUpdateInfor);
        fullname=(EditText) view.findViewById(R.id.edtFullNameUpdateInfor);
        number=(EditText) view.findViewById(R.id.edtNumberUpdateInfor);
        btnUpdate=(Button)view.findViewById(R.id.btnUpdateInfor);
        layoutUpdate=(LinearLayout) view.findViewById(R.id.LinearUpdate);
    }

    void setEffect(){
        Picasso.get()
                .load(DataProfilePage.userModel.getAvatarUrl())
                .into(avatarUser);
        nameUser.setText(DataProfilePage.userModel.getFullName());
        recyclerViewFarvoritePlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.Adapter adapterPlayList;
        adapterPlayList = new AlbumHomePageAdapter(DataProfilePage.arrFavoritePlaylist,this);
        recyclerViewFarvoritePlaylist.setAdapter(adapterPlayList);
        updateInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email.setText(DataProfilePage.userModel.getEmail());
                fullname.setText(DataProfilePage.userModel.getFullName());
                number.setText(DataProfilePage.userModel.getMobile());
                layoutUpdate.setVisibility(View.VISIBLE);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataProfilePage.arrFavoritePlaylist=new ArrayList<>();
                DataProfilePage.userModel=new UserModel();
                startActivity(new Intent(getActivity(),Login.class));
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();


                Map<String, Object> updates = new HashMap<>();
                updates.put("avatarUrl", DataProfilePage.userModel.getAvatarUrl());
                updates.put("email", email.getText().toString());
                updates.put("fullName", fullname.getText().toString());
                updates.put("mobile", number.getText().toString());
                db.collection("users").document(DataProfilePage.userModel.getUserUid()).update(updates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "Update Thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Update Thất Bại", Toast.LENGTH_SHORT).show();
                                    }
                                });

                layoutUpdate.setVisibility(View.GONE);
            }
        });

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Get(rootView);
        setEffect();
        return rootView;
    }


    @Override
    public void OnAlbumClick(PlaylistModel album) {
        Bundle bundle = new Bundle();
        bundle.putString("albumId", album.getPlaylistId());
        TopSongFragment topSongFragment=new TopSongFragment();
        topSongFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.FrameHomePage, topSongFragment).addToBackStack(null).commit();
    }
}

