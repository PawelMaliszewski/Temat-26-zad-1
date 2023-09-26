package com.tema26zad1.appservice;

import com.tema26zad1.bet.Bet;
import com.tema26zad1.betgame.BetGame;
import com.tema26zad1.betgame.BetGameRepository;
import com.tema26zad1.game.Game;
import com.tema26zad1.game.GameRepository;
import com.tema26zad1.game.GameResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final BetGameRepository betGameRepository;
    private final GameRepository gameRepository;

    public GameService(BetGameRepository betGameRepository, GameRepository gameRepository) {
        this.betGameRepository = betGameRepository;
        this.gameRepository = gameRepository;
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    public List<Game> gamesForBet() {
        List<Game> gamesForBet = new ArrayList<>();
        return gameRepository.findAllGamesThatAreNotEnded();
    }

    public Optional<Game> findGameById(Long gameId) {
        return gameRepository.findById(gameId);
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> fourMostFrequentBetGames() {
        List<Long> gameIds = betGameRepository.findAllGameIDsThatAreNotEndedBasedOnBetGames();
        List<Long> collectList = gameIds.stream()
                .collect(Collectors.groupingBy(Long::longValue, Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(4)
                .toList();
        collectList.forEach(System.out::println);
        return gameRepository.findAllById(collectList);
    }

    public void deleteGameById(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    public Boolean checkIfAnyGameEnded(@NotNull Bet bet) {
            return !gameRepository.findAllById(bet.getBetGames().stream()
                    .map(BetGame::getGameId).toList()).stream().
                    allMatch(game -> game.getGameResult().equals(GameResult.WAITING));
    }
}
