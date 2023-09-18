package com.tema26zad1.bet;

import com.tema26zad1.betgame.TempBetGame;
import jakarta.persistence.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TemporaryBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long betId;
    private BigDecimal betMoney;
    private BigDecimal earned;
    private Double rate;
    private BigDecimal moneyToWin;
    private boolean notActive;
    private final List<TempBetGame> tempBetGames = new ArrayList<>();

    public TemporaryBet() {
    }

    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public List<TempBetGame> getTempBetGames() {
        return tempBetGames;
    }

    public BigDecimal getBetMoney() {
        return betMoney;
    }

    public void setBetMoney(BigDecimal betMoney) {
        this.betMoney = betMoney;
    }

    public BigDecimal getEarned() {
        return earned;
    }

    public void setEarned(BigDecimal earned) {
        this.earned = earned;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
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

    public void clear() {
        betId = null;
        betMoney = null;
        earned = null;
        rate = null;
        moneyToWin = null;
        tempBetGames.clear();
    }
}
