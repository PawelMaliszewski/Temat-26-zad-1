package com.tema26zad1.appservice;

import com.tema26zad1.Result.GameResult;
import com.tema26zad1.bet.*;
import com.tema26zad1.betgame.BetGame;
import com.tema26zad1.betgame.BetGameRepository;
import com.tema26zad1.game.Game;
import com.tema26zad1.game.GameRepository;
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
                .limit(4)
                .toList();
        return gameRepository.findAllById(collectList);
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

    public Bet toWin(Bet bet, List<BetGame> betGameList) {
        double winRateSum = betGameList.stream().mapToDouble(BetGame::getWinRate).sum();
        winRateSum = (betGameList.size() == 1) ? winRateSum : winRateSum * 0.84;
        BigDecimal bigWinRate = new BigDecimal(winRateSum).setScale(2, RoundingMode.CEILING);
        bet.setRate(Double.parseDouble(bigWinRate.toString()));
        if (bet.getBetMoney() != null && Double.parseDouble(bet.getBetMoney().toString()) > 0) {
            BigDecimal sum = bet.getBetMoney().multiply(bigWinRate).setScale(2, RoundingMode.CEILING);
            bet.setMoneyToWin(sum.setScale(2, RoundingMode.CEILING));
        }
        return bet;
    }

    public void updateBets() {
        for (Bet bet : betRepository.findAll()) {
            if (!bet.isNotActive()) {
                List<BetGame> allGamesByBetBetId = betGameRepository.findAllByBet_BetId(bet.getBetId());
                boolean allMatchEnded = allGamesByBetBetId.stream()
                        .allMatch(betGame ->
                                gameRepository.findById(betGame.getGameId()).get().getGameResult() != GameResult.WAITING);
                if (allMatchEnded) {
                    bet.setNotActive(true);
                    boolean allResultsMatchBetsTyping = allGamesByBetBetId.stream()
                            .allMatch(betGame ->
                                    betGame.getGameResult() == gameRepository.findById(betGame.getGameId()).get().getGameResult());
                    if (!allResultsMatchBetsTyping) {
                        bet.setMoneyToWin(new BigDecimal(0));
                    }
                }
            }
            betRepository.save(bet);
        }
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
                bet = toWin(bet, bet.getBetGames());
                betRepository.save(bet);
            }
        }
        gameRepository.deleteById(gameId);
    }
}

