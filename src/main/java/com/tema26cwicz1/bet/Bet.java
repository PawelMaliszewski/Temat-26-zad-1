package com.tema26cwicz1.bet;

import com.tema26cwicz1.account.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long betId;
    private BigDecimal betMoney;
    @ManyToOne
    private Account account;
    @OneToMany(mappedBy = "bet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BetGame> betGames = new ArrayList<>();


    public void addBetToList(BetGame betGame) {
        betGames.add(betGame);
    }
}

