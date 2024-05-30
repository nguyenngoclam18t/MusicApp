package com.example.musicapp.Model;

public class SongModel {
    public String songId;
    public String Title;
    public String artistId;
    public String albumId;
    public String genreId;
    public String imgUrl;
    public String songUrl;

    public SongModel() {
    }

    public SongModel(String songId, String title, String artistId, String albumId, String genreId, String imgUrl, String songUrl) {
        this.songId = songId;
        Title = title;
        this.artistId = artistId;
        this.albumId = albumId;
        this.genreId = genreId;
        this.imgUrl = imgUrl;
        this.songUrl = songUrl;
    }
}
