package com.tema26cwicz1.appservice;

import com.tema26cwicz1.Result.GameResult;
import com.tema26cwicz1.account.AccountRepository;
import com.tema26cwicz1.bet.*;
import com.tema26cwicz1.game.Game;
import com.tema26cwicz1.game.GameRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppService {

    private final AccountRepository accountRepository;

    private final BetRepository betRepository;

    private final BetGameRepository betGameRepository;

    private final GameRepository gameRepository;

    public AppService(AccountRepository accountRepository, BetRepository betRepository, BetGameRepository betGameRepository, GameRepository gameRepository) {
        this.accountRepository = accountRepository;
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
                .limit(4)
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

    public List<Game> onlyUnselectedGames(Long betId) {
        List<Game> gamesLeft = gameRepository.findAll();
        List<Game> betGames = new ArrayList<>();
        Bet bet = betRepository.findById(betId).get();
        for (BetGame betGame : bet.getBetGames()) {
            betGames.add(gameRepository.findById(betGame.getGameId()).get());
        }
        gamesLeft.removeAll(betGames);
        System.out.println();
        return gamesLeft;
    }

    public BigDecimal toWin(Bet bet, BigDecimal betAmount) {
        BigDecimal winRateSum = new BigDecimal("0.00");
        List<BetGame> betGamesByBet = betGameRepository.findBetGamesByBet(bet);
        for (BetGame betGame : betGamesByBet) {
            winRateSum = winRateSum.add(BigDecimal.valueOf(betGame.getWinRate()));
        }
        BigDecimal sum = bet.getBetMoney().multiply((betGamesByBet.size() == 1) ?
                winRateSum : winRateSum.multiply(new BigDecimal("0.84")));
        return (betAmount.intValue() > 0) ? sum.setScale(2, RoundingMode.CEILING) : new BigDecimal("0");
    }
}

