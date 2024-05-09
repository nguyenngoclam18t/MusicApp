package com.example.musicapp.Model;

public class Singer {
    private  int Image;
    private  String Name;

    public Singer( String name,int image) {
        Image = image;
        Name = name;
    }
    public int getImage() {
        return Image;
    }
    public void setImage(int image) {
        Image = image;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
}
