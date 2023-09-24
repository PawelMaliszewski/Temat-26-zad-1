package com.tema26zad1.game;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;
    private String nameOfTeamA;
    private String nameOfTeamB;
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    private double teamAWinRate;
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    private double teamBWinRate;
    private double drawRate;
    @Enumerated(EnumType.STRING)
    private GameResult gameResult;

    public Game() {
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getNameOfTeamA() {
        return nameOfTeamA;
    }

    public void setNameOfTeamA(String nameOfTeamA) {
        this.nameOfTeamA = nameOfTeamA;
    }

    public String getNameOfTeamB() {
        return nameOfTeamB;
    }

    public void setNameOfTeamB(String nameOfTeamB) {
        this.nameOfTeamB = nameOfTeamB;
    }

    public double getTeamAWinRate() {
        return teamAWinRate;
    }

    public void setTeamAWinRate(double teamAWinRate) {
        this.teamAWinRate = teamAWinRate;
    }

    public double getTeamBWinRate() {
        return teamBWinRate;
    }

    public void setTeamBWinRate(double teamBWinRate) {
        this.teamBWinRate = teamBWinRate;
    }

    public double getDrawRate() {
        return drawRate;
    }

    public void setDrawRate(double drawRate) {
        this.drawRate = drawRate;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }

    public String gameTitle() {
        return nameOfTeamA + " VS " + nameOfTeamB;
    }

    public String groupName() {
        return "group" + gameId;
    }

    public String idForElementA() {
        return "ag" + gameId;
    }

    public String idForElementB() {
        return "bg" + gameId;
    }

    public String idForElementC() {
        return "cg" + gameId;
    }

    public String jsDataA() {
        return gameId + ";" + (nameOfTeamA + " vs " + nameOfTeamB) + ";" + nameOfTeamA + ";" + GameResult.TEAM_A_WON.name() + ";" + teamAWinRate + ";";
    }

    public String jsDataB() {
        return gameId + ";" + (nameOfTeamA + " vs " + nameOfTeamB) + ";" + "Remis" + ";" + GameResult.DRAW.name() + ";" + drawRate + ";";
    }

    public String jsDataC() {
        return gameId + ";" + (nameOfTeamA + " vs " + nameOfTeamB) + ";" + nameOfTeamB + ";" + GameResult.TEAM_B_WON.name() + ";" + teamBWinRate;
    }
}

