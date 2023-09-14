package com.tema26cwicz1.appservice;

import com.tema26cwicz1.Result.GameResult;
import com.tema26cwicz1.bet.*;
import com.tema26cwicz1.betgame.BetGame;
import com.tema26cwicz1.betgame.BetGameRepository;
import com.tema26cwicz1.game.Game;
import com.tema26cwicz1.game.GameRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppService {

    private final BetRepository betRepository;

    private final BetGameRepository betGameRepository;

    private final GameRepository gameRepository;

    public AppService(BetRepository betRepository, BetGameRepository betGameRepository, GameRepository gameRepository) {
        this.betRepository = betRepository;
        this.betGameRepository = betGameRepository;
        this.gameRepository = gameRepository;
    }

    public List<Game> gamesForBet(List<Game> gameList) {
        List<Game> gamesForBet = new ArrayList<>();
        for (Game game : gameList) {
            if (game.getGameResult().name().equals("WAITING")) {
                gamesForBet.add(game);
            }
        }
        return gamesForBet;
    }

    public List<Game> pastGames() {
        return (gameRepository.count() > 0) ? gameRepository.findAll().stream()
                .filter(bet -> !bet.getGameResult().equals(GameResult.WAITING))
                .collect(Collectors.toList()) : null;
    }

    public List<Game> fourMostFrequentBetGames() {
        List<Long> collectList = betGameRepository.findAll()
                .stream().collect(Collectors.groupingBy(BetGame::getGameId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(2)
                .toList();
        return gameRepository.findAllById(collectList);
    }

    public void deleteBet(Long id) {
        Optional<Bet> betById = betRepository.findById(id);
        betById.ifPresent(bet -> betGameRepository.deleteAll(bet.getBetGames()));
    }

    public double findWinRate(BetGame betGame, Game game) {
        if (betGame.getGameResult().name().equals("TEAM_A_WON")) {
            return game.getTeamAWinRate();
        } else if (betGame.getGameResult().name().equals("TEAM_B_WON")) {
            return game.getTeamBWinRate();
        }
        return game.getDrawRate();
    }

    public List<Game> onlyUnselectedGames(List<BetGame> betGameList) {
        List<Game> gamesLeft = gameRepository.findAll();
        List<Game> gamesAddedToBetCoupon = new ArrayList<>();
        for (BetGame betGame : betGameList) {
            gamesAddedToBetCoupon.add(gameRepository.findById(betGame.getGameId()).get());
        }
        gamesLeft.removeAll(gamesAddedToBetCoupon);
        System.out.println();
        return gamesLeft;
    }

    public BigDecimal toWin(BigDecimal betAmount, List<BetGame> betGameList) {
        if (Double.parseDouble(betAmount.toString()) > 0) {
            BigDecimal winRateSum = new BigDecimal("0.00");
            for (BetGame betGame : betGameList) {
                winRateSum = winRateSum.add(BigDecimal.valueOf(betGame.getWinRate()));
            }
            BigDecimal sum = betAmount.multiply((betGameList.size() == 1) ?
                    winRateSum : winRateSum.multiply(new BigDecimal("0.84")));
            return sum.setScale(2, RoundingMode.CEILING);
        }
        return new BigDecimal("0");
    }
}

