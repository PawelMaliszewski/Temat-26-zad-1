package com.tema26cwicz1.bet;

import com.tema26cwicz1.account.Account;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long betId;
    private BigDecimal betMoney;
    @ManyToOne
    private Account account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<BetGame> getBetGames() {
        return betGames;
    }

    public void setBetGames(List<BetGame> betGames) {
        this.betGames = betGames;
    }
}

