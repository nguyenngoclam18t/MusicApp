package com.example.musicapp.Model;

public class ArtistsModel {
    private String artistId;
    private String artistName;
    private String avatarUrl;

    public ArtistsModel(String artistId, String artistName, String avatarUrl) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.avatarUrl = avatarUrl;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
