package com.example.musicapp.Model;

import java.util.List;

public class UserModel {
    private String userUid,avatarUrl,fullName,email,mobile;
    private List<String> favoritePlaylists;

    public List<String> getFavoritePlaylists() {
        return favoritePlaylists;
    }

    public void setFavoritePlaylists(List<String> favoritePlaylists) {
        this.favoritePlaylists = favoritePlaylists;
    }

    public UserModel(String userUid, String avatarUrl, String fullName, String email, String mobile) {
        this.userUid = userUid;
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
    }

    public UserModel() {
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
