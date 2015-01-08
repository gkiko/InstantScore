package com.kuku.instantscore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gkiko on 6/29/14.
 */
public class League {
    @SerializedName("header")
    String name;

    @SerializedName("date")
    String date;

    @SerializedName("matches")
    List<com.kuku.instantscore.model.Match> matches;

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public List<com.kuku.instantscore.model.Match> getMatches() {
        return matches;
    }


}
