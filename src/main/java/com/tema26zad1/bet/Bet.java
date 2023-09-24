package com.tema26zad1.bet;

import com.tema26zad1.betgame.BetGame;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long betId;
    private BigDecimal betMoney;
    private Double rate;
    private BigDecimal moneyToWin;
    private boolean notActive;
    @OneToMany(mappedBy = "bet", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BetGame> betGames = new ArrayList<>();

    public Bet() {
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double totalRate) {
        this.rate = totalRate;
    }

    public BigDecimal getMoneyToWin() {
        return moneyToWin;
    }

    public void setMoneyToWin(BigDecimal moneyToWin) {
        this.moneyToWin = moneyToWin;
    }

    public boolean isNotActive() {
        return notActive;
    }

    public void setNotActive(boolean notActive) {
        this.notActive = notActive;
    }

    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public BigDecimal getBetMoney() {
        return betMoney;
    }

    public void setBetMoney(BigDecimal betMoney) {
        this.betMoney = betMoney;
    }

    public List<BetGame> getBetGames() {
        return betGames;
    }

    public void setBetGames(List<BetGame> betGames) {
        this.betGames = betGames;
    }
}

