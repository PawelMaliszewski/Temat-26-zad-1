package com.tema26zad1.appservice;

import com.tema26zad1.bet.Bet;
import com.tema26zad1.bet.BetRepository;
import com.tema26zad1.betgame.BetGame;
import com.tema26zad1.betgame.BetGameRepository;
import com.tema26zad1.game.Game;
import com.tema26zad1.game.GameRepository;
import com.tema26zad1.game.GameResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BetService {

    private final BetRepository betRepository;
    private final BetGameRepository betGameRepository;
    private final GameRepository gameRepository;

    public BetService(BetRepository betRepository, BetGameRepository betGameRepository, GameRepository gameRepository) {
        this.betRepository = betRepository;
        this.betGameRepository = betGameRepository;
        this.gameRepository = gameRepository;
    }

    public List<BetGame> findAllByGameId(Long gameId) {
        return betGameRepository.findAllByGameId(gameId);
    }

    public List<BetGame> findAllBetGames() {
        return betGameRepository.findAll();
    }

    public List<Bet> findAllBets() {
        return betRepository.findAll();
    }

    public BetRepository getBetRepository() {
        return betRepository;
    }

    public BetGameRepository getBetGameRepository() {
        return betGameRepository;
    }

    public double findWinRate(@NotNull BetGame betGame, Game game) {
        if (betGame.getGameResult().name().equals("TEAM_A_WON")) {
            return game.getTeamAWinRate();
        } else if (betGame.getGameResult().name().equals("TEAM_B_WON")) {
            return game.getTeamBWinRate();
        }
        return game.getDrawRate();
    }

    public Bet toWin(@NotNull Bet bet) {
        List<BetGame> betGameList = bet.getBetGames();
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

    public void updateBetsThatAreContainThisGame(@NotNull Game game) {
        List<Bet> betListIncludingEditedGame = betRepository.findAllById(
                betGameRepository.findAllByGameId(game.getGameId()).stream()
                        .map(betGame -> betGame.getBet().getBetId()).toList());
        for (Bet bet : betListIncludingEditedGame) {
            for (BetGame betGame : bet.getBetGames()) {
                if (betGame.getGameId().equals(game.getGameId())) {
                    betGame.setGameTitle(game.getNameOfTeamA() + " vs " + game.getNameOfTeamB());
                    betGame.setWinRate(findWinRate(betGame, game));
                }
            }
            bet = toWin(bet);
            betRepository.save(bet);
        }
        for (Bet bet : betListIncludingEditedGame) {
            setBetAsNotActiveWithAnEventualWinIfAllGamesHaveEnded(bet);
            betRepository.save(bet);
        }
    }

    public void setBetAsNotActiveWithAnEventualWinIfAllGamesHaveEnded(Bet bet) {
        if (!bet.isNotActive()) {
            List<BetGame> allGamesByBetBetId = bet.getBetGames();
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
    }

    public void deleteBetIfOneBetGameOrJustBetGameByGameId(@NotNull List<BetGame> betGamesByGameId) {
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
                bet = toWin(bet);
                setBetAsNotActiveWithAnEventualWinIfAllGamesHaveEnded(bet);
                betRepository.save(bet);
            }
        }
    }

    public void saveBetAndSetValues(Bet bet) {
        bet = setValuesForTheBet(bet);
        betRepository.save(bet);
    }

    public Bet setValuesForTheBet(Bet bet) {
        bet.getBetGames().forEach(betGame -> betGame.setBet(bet));
        for (BetGame betGame : bet.getBetGames()) {
            findWinRate(betGame, gameRepository.findById(betGame.getGameId()).get());
        }
        return toWin(bet);
    }
}