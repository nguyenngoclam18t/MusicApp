package com.example.musicapp.Model;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class FireStoreDB {
    public static ArrayList<SongModel> arrSong = new ArrayList<>();
    public static ArrayList<ArtistsModel> arrArtists = new ArrayList<>();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void initializeData(final FirestoreCallback callback) {
        db.collection("songs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            synchronized (arrSong) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String imgUrl = document.getString("imgUrl");
                                    String genreId = document.getString("genreId");
                                    String albumId = document.getString("albumId");
                                    String artistId = document.getString("artistId");
                                    String title = document.getString("title");
                                    String songUrl = document.getString("songUrl");
                                    arrSong.add(new SongModel(document.getId(), title, artistId, albumId, genreId, imgUrl, songUrl));
                                }
                            }
                            fetchArtists(callback); // Fetch artists after songs are fetched
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private static void fetchArtists(final FirestoreCallback callback) {
        db.collection("artists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            synchronized (arrArtists) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String avatarUrl = document.getString("avatarUrl");
                                    arrArtists.add(new ArtistsModel(document.getId(), avatarUrl));
                                }
                            }
                            callback.onCallback();
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
