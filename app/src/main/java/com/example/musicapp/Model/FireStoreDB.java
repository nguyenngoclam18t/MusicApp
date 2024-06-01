package com.example.musicapp.Model;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;

public class FireStoreDB {
    public static ArrayList<SongModel> arrSong = new ArrayList<>();
    public static ArrayList<ArtistsModel> arrArtists = new ArrayList<>();
    public static ArrayList<AlbumModel> arrAlbum = new ArrayList<>();
    public static ArrayList<GenreModel> arrGenre = new ArrayList<>();
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
                                Collections.shuffle(arrSong);

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
                                Collections.shuffle(arrArtists);

                            }
                            fetchGenre(callback);
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private static void fetchGenre(final FirestoreCallback callback) {
        db.collection("genres")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            synchronized (arrGenre) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    arrGenre.add(new GenreModel(document.getId()));
                                }
                                Collections.shuffle(arrGenre);
                            }
                            fetchAlbums(callback);
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private static void fetchAlbums(final FirestoreCallback callback) {
        db.collection("albums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            synchronized (arrAlbum) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String artistId = document.getString("artistID");
                                    String imgUrl = document.getString("imageUrl");
                                    arrAlbum.add(new AlbumModel(document.getId(),artistId, imgUrl));
                                }
                                Collections.shuffle(arrAlbum);
                            }
                            callback.onCallback();
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
