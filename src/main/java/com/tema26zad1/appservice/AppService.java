package com.tema26zad1.appservice;

import com.tema26zad1.bet.*;
import com.tema26zad1.betgame.BetGame;
import com.tema26zad1.game.GameRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppService {

    private final BetRepository betRepository;
    private final GameRepository gameRepository;
    private final BetService betService;

    public AppService(BetRepository betRepository, GameRepository gameRepository, BetService betService) {
        this.betRepository = betRepository;
        this.gameRepository = gameRepository;
        this.betService = betService;
    }

    public void deleteGameAndBetAndBetGamesRelatedToGameId(Long gameId, List<BetGame> betGamesByGameId) {
        List<BetGame> oneBetGameOnBetList = betGamesByGameId.stream()
                .filter(betGame -> betRepository.findById(betGame.getBet().getBetId()).get().getBetGames().size() == 1).toList();
        if (!oneBetGameOnBetList.isEmpty()) {
            for (BetGame betGame : oneBetGameOnBetList) {
                Bet bet = betRepository.findById(betGame.getBet().getBetId()).get();
                betRepository.delete(bet);
            }
        } else {
            for (BetGame betGame : betGamesByGameId) {
                Bet bet = betRepository.findById(betGame.getBet().getBetId()).get();
                bet.getBetGames().remove(betGame);
                bet = betService.toWin(bet);
                betRepository.save(bet);
            }
        }
        gameRepository.deleteById(gameId);
    }
}

