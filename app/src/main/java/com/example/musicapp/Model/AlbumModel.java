package com.example.musicapp.Model;

public class AlbumModel {
    public String albumId;
    public String artistID;
    public String imgUrl;

    public AlbumModel() {
    }

    public AlbumModel(String albumId, String artistID, String imgUrl) {
        this.albumId = albumId;
        this.artistID = artistID;
        this.imgUrl = imgUrl;
    }
}
