package com.tema26zad1.betgame;

import com.tema26zad1.game.GameResult;
import com.tema26zad1.bet.Bet;
import jakarta.persistence.*;

@Entity
public class BetGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gameId;
    private String gameTitle;
    private String betFor;
    @Enumerated(EnumType.STRING)
    private GameResult gameResult;
    private double winRate;
    @ManyToOne
    @JoinColumn(name = "bet_id")
    private Bet bet;

    public BetGame() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public Bet getBet() {
        return bet;
    }

    public void setBet(Bet bet) {
        this.bet = bet;
    }

    public String getBetFor() {
        return betFor;
    }

    public void setBetFor(String betFor) {
        this.betFor = betFor;
    }

    @Override
    public String toString() {
        return "BetGame{" +
               "gameId=" + gameId +
               ", gameTitle='" + gameTitle + '\'' +
               ", betFor='" + betFor + '\'' +
               ", gameResult=" + gameResult +
               ", winRate=" + winRate +
               ", bet=" + bet +
               '}';
    }
}
