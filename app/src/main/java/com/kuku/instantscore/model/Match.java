package com.kuku.instantscore.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkiko on 6/29/14.
 */
public class Match {
    @SerializedName("time")
    String time;

    @SerializedName("t1")
    String team1;

    @SerializedName("score")
    String score;

    @SerializedName("t2")
    String team2;

    String id = null;

    boolean marked = false;

    public Match(String time, String team1, String score, String team2){
        this.time = time;
        this.team1 = team1;
        this.score = score;
        this.team2 = team2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getId(){
        if(id == null){
            id = team1 + " vs " + team2;
        }
        return id;
    }

    public boolean isMarked(){
        return marked;
    }

    public void toggleMark(){
        marked = !marked;
    }
}
