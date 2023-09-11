package com.tema26cwicz1.controllers;

import com.tema26cwicz1.game.GameRepository;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
}
