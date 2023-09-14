package com.tema26cwicz1.bet;

import com.tema26cwicz1.betgame.BetGame;
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
    private BigDecimal earned;
    @OneToMany(mappedBy = "bet", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<BetGame> betGames = new ArrayList<>();

    public Bet() {
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

    public BigDecimal getEarned() {
        return earned;
    }

    public void setEarned(BigDecimal earned) {
        this.earned = earned;
    }
}

