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

    public Game findGameById(Long gameId) {
        Optional<Game> byId = gameRepository.findById(gameId);
        return byId.orElse(null);
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> fourMostFrequentBetGames() {
        List<Long> collectList = betGameRepository.findAll()
                .stream()
                .filter(betGame -> gameRepository.findById(betGame.getGameId()).isPresent())
                .filter(betGame -> gameRepository.findById(betGame.getGameId()).get().getGameResult().equals(GameResult.WAITING))
                .collect(Collectors.groupingBy(BetGame::getGameId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(4)
                .toList();
        return gameRepository.findAllById(collectList);
    }

    public void deleteGameById(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    public List<Game> listOfGamesThatAreEnded() {
        List<Game> allGamesById = gameRepository.findAll();
        return allGamesById.stream().filter(game -> !game.getGameResult().equals(GameResult.WAITING)).toList();
    }

    public Boolean checkIfAnyGameEnded(@NotNull Bet bet) {
        return bet.getBetGames().stream().noneMatch(betGame ->
                gameRepository.findById(betGame.getGameId()).get().getGameResult().equals(GameResult.WAITING));
    }
}
