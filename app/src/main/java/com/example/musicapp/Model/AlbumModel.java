package com.example.musicapp.Model;

public class AlbumModel {
    public String albumId;
    public String albumName;

    public String artistId;
    public String imageUrl;

    public AlbumModel() {

    }

    public AlbumModel(String albumId, String albumName, String artistId, String imageUrl) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.imageUrl = imageUrl;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
