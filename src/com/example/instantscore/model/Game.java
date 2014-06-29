package com.example.instantscore.model;

public class Game {
    private boolean isSelected;
    private String tournament;
    private String date;
    private String time;
    private String homeTeam;
    private String awayTeam;
    private String homeTeamScore;
    private String awayTeamScore;

    public Game() {
        isSelected = false;
    }

    @Override
    public String toString() {
        return new StringBuilder("Game [country=").append(getTournament()).append(", date=").append(getDate()).append(", time=").append(getTime())
                .append(", homeTeam=").append(getHomeTeam()).append(", awayTeam=").append(getAwayTeam())
                .append(", homeTeamScore=").append(getHomeTeamScore()).append(", awayTeamScore=")
                .append(getAwayTeamScore()).append(", isSelected=").append(isSelected()).append("]").toString();
    }

    public String getGameId() {
        return getHomeTeam() + " vs " + getAwayTeam();
    }

    public boolean isSelectable() {
        return !getTime().equals("FT") && !getTime().equals("Postp.") && !getTime().equals("AAW") && !getTime().equals("AET");
    }

    @Override
    public boolean equals(Object o) {
        Game other = (Game) o;
        return getGameId().equals(other.getGameId());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getTournament() {
        return tournament;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
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
}
