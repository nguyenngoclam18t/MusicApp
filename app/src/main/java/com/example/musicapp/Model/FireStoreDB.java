package com.example.musicapp.Model;

import android.nfc.Tag;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

public class FireStoreDB {
    public static ArrayList<SongModel> arrSong = new ArrayList<>();
    public static ArrayList<ArtistsModel> arrArtists = new ArrayList<>();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "FireStoreDB";

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
                                    String artistName = document.getString("artistName");
                                    String avatarUrl = document.getString("avatarUrl");
                                    arrArtists.add(new ArtistsModel(document.getId(), artistName, avatarUrl));
                                }
                            }
                            callback.onCallback();
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static void getSongsByArtist(String artistId, final FirestoreCallback callback) {
        db.collection("songs")
                .whereEqualTo("artistId", artistId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<SongModel> songs = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String songId = document.getId();
                                SongModel song = document.toObject(SongModel.class);
                                song.setSongId(songId);
                                songs.add(song);
                            }
                            callback.onSongsCallback(songs);
                        } else {

                        }
                    }
                });
    }

    public static void getAlbumsByArtist(String artistId, final FirestoreCallback callback) {
        db.collection("albums")
                .whereEqualTo("artistID", artistId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<AlbumModel> albums = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AlbumModel album = document.toObject(AlbumModel.class);
                                albums.add(album);
                            }
                            Log.d("FirestoreData", "Albums fetched: " + albums.size());
                            callback.onAlbumsCallback(albums);
                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
