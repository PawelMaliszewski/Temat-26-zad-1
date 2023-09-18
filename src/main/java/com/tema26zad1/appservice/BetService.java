package com.tema26zad1.appservice;

import com.tema26zad1.bet.Bet;
import com.tema26zad1.bet.BetRepository;
import com.tema26zad1.bet.TemporaryBet;
import com.tema26zad1.betgame.TempBetGame;
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

    public void saveBet(Bet bet) {
        betRepository.save(bet);
    }

    public List<Bet> findAllBets() {
        return betRepository.findAll();
    }

    public double findWinRate(TempBetGame tempBetGame, BetGame betGame, Game game) {
        BetGame betGame1 = (tempBetGame == null) ? betGame : convertToBetGame(tempBetGame);
        if (betGame1.getGameResult().name().equals("TEAM_A_WON")) {
            return game.getTeamAWinRate();
        } else if (betGame1.getGameResult().name().equals("TEAM_B_WON")) {
            return game.getTeamBWinRate();
        }
        return game.getDrawRate();
    }

    public TemporaryBet toWinTempBet(@NotNull TemporaryBet temporaryBet) {
        List<TempBetGame> tempoBetGames = temporaryBet.getTempBetGames();
        double winRateSum = tempoBetGames.stream().mapToDouble(TempBetGame::getWinRate).sum();
        winRateSum = (tempoBetGames.size() == 1) ? winRateSum : winRateSum * 0.84;
        BigDecimal bigWinRate = new BigDecimal(winRateSum).setScale(2, RoundingMode.CEILING);
        temporaryBet.setRate(Double.parseDouble(bigWinRate.toString()));
        if (temporaryBet.getBetMoney() != null && Double.parseDouble(temporaryBet.getBetMoney().toString()) > 0) {
            BigDecimal sum = temporaryBet.getBetMoney().multiply(bigWinRate).setScale(2, RoundingMode.CEILING);
            temporaryBet.setMoneyToWin(sum.setScale(2, RoundingMode.CEILING));
        }
        return temporaryBet;
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

    public void updateBets(@NotNull Game game) {
        List<Bet> betListIncludingEditedGame = betRepository.findAllById(
                betGameRepository.findAllByGameId(game.getGameId()).stream()
                        .map(betGame -> betGame.getBet().getBetId()).toList());
        for (Bet bet : betListIncludingEditedGame) {
            for (BetGame betGame : bet.getBetGames()) {
                if (betGame.getGameId().equals(game.getGameId())) {
                    betGame.setGameTitle(game.getNameOfTeamA() + " VS " + game.getNameOfTeamB());
                    betGame.setWinRate(findWinRate(null, betGame, game));
                }
            }
            bet = toWin(bet);
            betRepository.save(bet);
        }
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

    @org.jetbrains.annotations.NotNull
    private BetGame convertToBetGame(@NotNull TempBetGame tempBetGame) {
        BetGame betGame = new BetGame();
        betGame.setGameTitle(tempBetGame.getGameTitle());
        betGame.setGameResult(tempBetGame.getGameResult());
        betGame.setWinRate(tempBetGame.getWinRate());
        return betGame;
    }

    public Bet convertToBetGame(@NotNull TemporaryBet temporaryBet) {
        Bet bet = new Bet();
        bet.setBetMoney(temporaryBet.getBetMoney());
        bet.setRate(temporaryBet.getRate());
        bet.setMoneyToWin(temporaryBet.getMoneyToWin());
        for (TempBetGame tgb : temporaryBet.getTempBetGames()) {
            BetGame betGame = new BetGame();
            betGame.setGameId(tgb.getGameId());
            betGame.setGameTitle(tgb.getGameTitle());
            betGame.setGameResult(tgb.getGameResult());
            betGame.setWinRate(tgb.getWinRate());
            betGame.setBet(bet);
            bet.getBetGames().add(betGame);
        }
        return bet;
    }
}