package com.example.instantscore.model;

/**
 * Created by gkiko on 8/25/14.
 */
public class Item {
    private String countryPrefix;
    private String countryName;
    private String countryIso;
    private int imgId;

    public Item(String countryPrefix, String countryName, String countryIso, int imgId){
        this.countryPrefix = countryPrefix;
        this.countryName = countryName;
        this.countryIso = countryIso;
        this.imgId = imgId;
    }

    public String getCountryPrefix() {
        return countryPrefix;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public int getImgId() {
        return imgId;
    }

    public void setCountryPrefix(String countryPrefix) {
        this.countryPrefix = countryPrefix;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
