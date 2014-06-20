package com.example.instantscore.model;

import com.example.instantscore.utils.Utils;

public class Game {
    private boolean isSelected = false;
    private String tournament, date, time, homeTeam, awayTeam, homeTeamScore, awayTeamScore;

    public String getTournament() {
        return tournament;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(String homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public String getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(String awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return new StringBuilder("Game [country=").append(tournament).append(", date=").append(date).append(", time=").append(time)
                .append(", homeTeam=").append(homeTeam).append(", awayTeam=").append(awayTeam)
                .append(", homeTeamScore=").append(homeTeamScore).append(", awayTeamScore=")
                .append(awayTeamScore).append(", isSelected=").append(isSelected).append("]").toString();
    }

    public String getGameId() {
        return homeTeam + " vs " + awayTeam;
    }

    public boolean isSelectable() {
        return !time.equals("FT") && !time.equals("Postp.") && !time.equals("AAW") && !time.equals("AET");
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        Game other = (Game) o;
        return getGameId().equals(other.getGameId());
    }

}
