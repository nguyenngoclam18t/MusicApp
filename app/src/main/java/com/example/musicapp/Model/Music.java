package com.example.musicapp.Model;

public class Music {
    private String title,singer;
    private  int Image;

    public Music(String title, String singer, int image) {
        this.title = title;
        this.singer = singer;
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
