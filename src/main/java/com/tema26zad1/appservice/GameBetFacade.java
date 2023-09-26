package com.tema26zad1.appservice;

import com.tema26zad1.betgame.BetGame;
import com.tema26zad1.game.Game;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameBetFacade {

    private final GameService gameService;
    private final BetService betService;

    public GameBetFacade(GameService gameService, BetService betService) {
        this.gameService = gameService;
        this.betService = betService;
    }

    public void deleteGameAndBetAndBetGamesRelatedToGameId(@NotNull Long gameId, @NotNull List<BetGame> listOfBetGamesByGameId) {
        betService.deleteBetIfOneBetGameOrJustBetGameByGameId(listOfBetGamesByGameId);
        gameService.deleteGameById(gameId);
    }

    public void saveAndUpdateBetsThatAreContainThisGame(Game game) {
        gameService.saveGame(game);
        betService.updateBetsThatAreContainThisGame(game);
    }
}

