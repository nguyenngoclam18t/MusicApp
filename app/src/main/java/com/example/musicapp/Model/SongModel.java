package com.example.musicapp.Model;

public class SongModel {
    private String songId;
    private String title;
    private String artistsNames;
    private String releaseDate;
    private String thumbnailLm;

    public SongModel(String songId, String title, String artistsNames, String releaseDate, String thumbnailLm) {
        this.songId = songId;
        this.title = title;
        this.artistsNames = artistsNames;
        this.releaseDate = releaseDate;
        this.thumbnailLm = thumbnailLm;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistsNames() {
        return artistsNames;
    }

    public void setArtistsNames(String artistsNames) {
        this.artistsNames = artistsNames;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getThumbnailLm() {
        return thumbnailLm;
    }

    public void setThumbnailLm(String thumbnailLm) {
        this.thumbnailLm = thumbnailLm;
    }
}
