package com.example.musicapp.Model;

public class Singer {
    private  int Image;
    private  String Name,About;


    public Singer(String name, String about, int image) {
        Image = image;
        Name = name;
        About=about;
    }
    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
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
