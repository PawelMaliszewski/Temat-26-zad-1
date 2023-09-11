package com.tema26cwicz1.game;



import com.tema26cwicz1.Result.GameResult;
import com.tema26cwicz1.bet.BetGame;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;
    private String nameOfTeamA;
    private String nameOfTeamB;
    private double teamAWinRate;
    private double teamBWinRate;
    private double drawRate;
    @Enumerated(EnumType.STRING)
    private GameResult gameResult;

}
