package com.example.musicapp.Model;

import java.io.Serializable;
import java.util.List;

public class PlaylistModel implements Serializable {
    private String playlistId;
    private String playlistName;
    private String thumbnailLm;
    private String sortDescription;
    private List<ArtistsModel> artists;

    public PlaylistModel() {
    }

    public PlaylistModel(String playlistId, String playlistName, String thumbnailLm, String sortDescription, List<ArtistsModel> artists) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.thumbnailLm = thumbnailLm;
        this.sortDescription = sortDescription;
        this.artists = artists;
    }

    // New constructor with four parameters
    public PlaylistModel(String playlistId, String playlistName, String thumbnailLm, String sortDescription) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.thumbnailLm = thumbnailLm;
        this.sortDescription = sortDescription;
        this.artists = null; // or new ArrayList<>(); if you prefer an empty list instead of null
    }

    public String getPlaylistId() { return playlistId; }
    public void setPlaylistId(String playlistId) { this.playlistId = playlistId; }

    public String getPlaylistName() { return playlistName; }
    public void setPlaylistName(String playlistName) { this.playlistName = playlistName; }

    public String getThumbnailLm() { return thumbnailLm; }
    public void setThumbnailLm(String thumbnailLm) { this.thumbnailLm = thumbnailLm; }

    public String getSortDescription() { return sortDescription; }
    public void setSortDescription(String sortDescription) { this.sortDescription = sortDescription; }

    public List<ArtistsModel> getArtists() { return artists; }
    public void setArtists(List<ArtistsModel> artists) { this.artists = artists; }
}
