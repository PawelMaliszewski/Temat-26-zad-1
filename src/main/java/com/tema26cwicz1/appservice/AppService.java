package com.tema26cwicz1.appservice;

import com.tema26cwicz1.Result.Result;
import com.tema26cwicz1.account.AccountRepository;
import com.tema26cwicz1.bet.BetGame;
import com.tema26cwicz1.bet.BetGameRepository;
import com.tema26cwicz1.bet.BetRepository;
import com.tema26cwicz1.game.Game;
import com.tema26cwicz1.game.GameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<Game> gamesForBet() {
        return (gameRepository.count() > 0) ? gameRepository.findAll().stream()
                .filter(bet -> bet.getResult().equals(Result.WAITING))
                .collect(Collectors.toList()) : null;
    }

    public List<Game> pastGames() {
        return (gameRepository.count() > 0) ? gameRepository.findAll().stream()
                .filter(bet -> !bet.getResult().equals(Result.WAITING))
                .collect(Collectors.toList()) : null;
    }

    public List<Game> findTop4Bets() {
        List<Game> top4 = new ArrayList<>();
        for (BetGame betGame : betGameRepository.findFirst4ByOrderByGameId()) {
            top4.add(gameRepository.getReferenceById(betGame.getId()));
        }
        return top4;
    }


}
