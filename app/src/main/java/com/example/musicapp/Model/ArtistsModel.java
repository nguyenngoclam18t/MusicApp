package com.example.musicapp.Model;

public class ArtistsModel {
    private String artistId;
    private String artistName;
    private String artistAliasName;
    private String sortBiography;
    private String thumbnailLm;

    public ArtistsModel(String artistId, String artistName, String artistAliasName, String sortBiography, String thumbnailLm) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistAliasName = artistAliasName;
        this.sortBiography = sortBiography;
        this.thumbnailLm = thumbnailLm;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistAliasName() {
        return artistAliasName;
    }

    public void setArtistAliasName(String artistAliasName) {
        this.artistAliasName = artistAliasName;
    }

    public String getSortBiography() {
        return sortBiography;
    }

    public void setSortBiography(String sortBiography) {
        this.sortBiography = sortBiography;
    }

    public String getThumbnailLm() {
        return thumbnailLm;
    }

    public void setThumbnailLm(String thumbnailLm) {
        this.thumbnailLm = thumbnailLm;
    }
}
