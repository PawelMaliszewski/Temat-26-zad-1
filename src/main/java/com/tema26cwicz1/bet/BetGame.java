package com.tema26cwicz1.bet;


import com.tema26cwicz1.Result.GameResult;
import com.tema26cwicz1.game.Game;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class BetGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gameId;
    private String gameTitle;
    @Enumerated(EnumType.STRING)
    private GameResult gameResult;
    private double winRate;
    @ManyToOne
    @JoinColumn(name = "bet_id")
    private Bet bet;

}
