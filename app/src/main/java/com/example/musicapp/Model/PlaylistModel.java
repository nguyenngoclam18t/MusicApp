package com.example.musicapp.Model;

public class PlaylistModel {
    private String PlaylistId;
    private String PlaylistName;
    private String thumbnailLm;
    private String releaseDate;
    private String sortDescription;
    public PlaylistModel() {

    }

    public PlaylistModel(String playlistId, String playlistName, String thumbnailLm, String releaseDate, String sortDescription) {
        PlaylistId = playlistId;
        PlaylistName = playlistName;
        this.thumbnailLm = thumbnailLm;
        this.releaseDate = releaseDate;
        this.sortDescription = sortDescription;
    }

    public String getPlaylistId() {
        return PlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        PlaylistId = playlistId;
    }

    public String getPlaylistName() {
        return PlaylistName;
    }

    public void setPlaylistName(String playlistName) {
        PlaylistName = playlistName;
    }

    public String getThumbnailLm() {
        return thumbnailLm;
    }

    public void setThumbnailLm(String thumbnailLm) {
        this.thumbnailLm = thumbnailLm;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSortDescription() {
        return sortDescription;
    }

    public void setSortDescription(String sortDescription) {
        this.sortDescription = sortDescription;
    }
}
