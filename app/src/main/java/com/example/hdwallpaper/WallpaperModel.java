package com.example.hdwallpaper;

public class WallpaperModel {

    private int id;
    private String originalUrl, mediulurl;

    public WallpaperModel() {
    }

    public WallpaperModel(int id, String originalUrl, String mediulurl) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.mediulurl = mediulurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getMediulurl() {
        return mediulurl;
    }

    public void setMediulurl(String mediulurl) {
        this.mediulurl = mediulurl;
    }
}
