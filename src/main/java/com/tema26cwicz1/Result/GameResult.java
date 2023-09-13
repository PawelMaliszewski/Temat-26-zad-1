package com.tema26cwicz1.Result;

public enum GameResult {
    TEAM_A_WON("drużyna gospodarzy zwyciężyła"), TEAM_B_WON("drużyna gości zwyciężyła"),
    DRAW("remis"), WAITING("gra sie nie skończyła");

    private final String description;

    GameResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
