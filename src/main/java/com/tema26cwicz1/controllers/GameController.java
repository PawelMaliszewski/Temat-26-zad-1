package com.tema26cwicz1.controllers;

import com.tema26cwicz1.bet.Bet;
import com.tema26cwicz1.bet.BetGame;
import com.tema26cwicz1.game.Game;
import com.tema26cwicz1.game.GameRepository;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }




}
