package com.tema26cwicz1.bet;


import com.tema26cwicz1.Result.Result;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class BetGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gameId;
    @Enumerated(EnumType.ORDINAL)
    private Result result;
    @ManyToMany(mappedBy = "betGames", fetch = FetchType.EAGER)
    private List<Bet> bets;
}
