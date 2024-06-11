package com.example.musicapp.Model;

import java.io.Serializable;
import java.util.List;

public class SongModel implements Serializable {
    private String songId;
    private String title;
    private String artistsNames;
    private String thumbnailLm;
    private String songUrl;

    private int duration;

    private static List<SongModel> allSongs;

    public SongModel(String songId, String title, String artistsNames, String thumbnailLm, String songUrl, int duration) {
        this.songId = songId;
        this.title = title;
        this.artistsNames = artistsNames;
        this.thumbnailLm = thumbnailLm;
        this.songUrl = songUrl;
        this.duration = duration;
    }



    public static void setAllSongs(List<SongModel> songs) {
        allSongs = songs;
    }

    public static List<SongModel> getAllSongs() {
        return allSongs;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public String getThumbnailLm() {
        return thumbnailLm;
    }

    public void setThumbnailLm(String thumbnailLm) {
        this.thumbnailLm = thumbnailLm;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }



    public static SongModel getNextSong(SongModel currentSong) {
        if (allSongs != null && !allSongs.isEmpty()) {
            int currentIndex = allSongs.indexOf(currentSong);
            if (currentIndex != -1 && currentIndex < allSongs.size() - 1) {
                return allSongs.get(currentIndex + 1);
            }
        }
        return null;
    }

    public static SongModel getPreviousSong(SongModel currentSong) {
        if (allSongs != null && !allSongs.isEmpty()) {
            int currentIndex = allSongs.indexOf(currentSong);
            if (currentIndex > 0) {
                return allSongs.get(currentIndex - 1);
            }
        }
        return null;
    }
}