package com.tema26cwicz1.bet;

import com.tema26cwicz1.Result.Result;
import com.tema26cwicz1.account.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Bet")
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long betId;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToMany
    @JoinColumn(name = "bet_List_id")
    private List<BetGame> betGames = new ArrayList<>();
    private BigDecimal betMoney;

    public void addBetToList(Long gameId, Result result) {
        BetGame betGame = new BetGame();
        betGame.setGameId(gameId);
        betGame.setResult(result);
        betGames.add(betGame);
    }

}
