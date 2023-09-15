package com.tema26zad1.controllers;

import com.tema26zad1.Result.GameResult;
import com.tema26zad1.appservice.AppService;
import com.tema26zad1.bet.Bet;
import com.tema26zad1.bet.BetRepository;
import com.tema26zad1.betgame.BetGame;
import com.tema26zad1.betgame.BetGameRepository;
import com.tema26zad1.game.Game;
import com.tema26zad1.game.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class GameController {

    private final BetRepository betRepository;
    private final GameRepository gameRepository;
    private final BetGameRepository betGameRepository;
    private final AppService appService;

    public GameController(BetRepository betRepository, GameRepository gameRepository, BetGameRepository betGameRepository, AppService appService) {
        this.betRepository = betRepository;
        this.gameRepository = gameRepository;
        this.betGameRepository = betGameRepository;
        this.appService = appService;
    }

    @GetMapping("/game_list")
    public String gameList(Model model) {
        model.addAttribute("gameList", gameRepository.findAll());
        return "game_list";
    }

    @PostMapping("/delete_game")
    public String deleteGame(@RequestParam Long gameId, @RequestParam(required = false) String yes, RedirectAttributes ra) {
        if (gameId != null) {
            List<BetGame> betGamesByGameId = betGameRepository.findAllByGameId(gameId);
            if (betGamesByGameId.isEmpty()) {
                betGameRepository.deleteById(gameId);
            } else {
                boolean isThereAnyActiveBetForThisGame = betGamesByGameId.stream().noneMatch(betGame -> betGame.getBet().isNotActive());
                if (!isThereAnyActiveBetForThisGame) {
                    appService.deleteGameAndBetAndBetGamesRelatedToGameId(gameId, betGamesByGameId);
                    return "redirect:/game_list";
                } else {
                    if (yes == null) {
                        ra.addAttribute("gameId", gameId);
                        return "redirect:warning";
                    } else {
                        appService.deleteGameAndBetAndBetGamesRelatedToGameId(gameId, betGamesByGameId);
                        return "redirect:/game_list";
                    }
                }
            }
        }
        return "index";
    }

    @GetMapping("/warning")
    private String test(@RequestParam  Long gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "warning";
    }

    @PostMapping("/add_edit")
    public String editGame(@RequestParam(required = false) Long gameId, Model model) {
        Game game = null;
        if (gameId != null) {
            Optional<Game> optionalGame = gameRepository.findById(gameId);
            if (optionalGame.isPresent()) {
                game = optionalGame.get();
                model.addAttribute("game", game);
                List<Bet> betListIncludingEditedGame = betRepository.findAllById(
                        betGameRepository.findAllByGameId(gameId).stream()
                                .map(betGame -> betGame.getBet().getBetId()).toList());
                for (Bet bet : betListIncludingEditedGame) {
                    bet = appService.toWin(bet, bet.getBetGames());
                    for (BetGame betGame : bet.getBetGames()) {
                        if (betGame.getGameId().equals(gameId)) {
                            betGame.setWinRate(appService.findWinRate(betGame, game));
                        }
                    }
                    betRepository.save(bet);
                }
            }
        } else {
            model.addAttribute("game", new Game());
        }
        return "/add_edit_match";
    }

    @PostMapping("/save_game")
    public String saveGame(@ModelAttribute Game game, Model model) {
        gameRepository.save(game);
        model.addAttribute("gameList", gameRepository.findAll());
        return "redirect:/game_list";
    }

    @PostMapping("/random_result")
    public String randomResult(@RequestParam Long gameId, Model model) {
        if (gameId != null) {
            Optional<Game> optionalGame = gameRepository.findById(gameId);
            Random random = new Random();
            GameResult gameResult = GameResult.values()[random.nextInt(0, 3)];
            if (optionalGame.isPresent()) {
                Game game = optionalGame.get();
                game.setGameResult(gameResult);
                gameRepository.saveAndFlush(optionalGame.get());
            }
        }
        model.addAttribute("gameList", gameRepository.findAll());
        return "redirect:/game_list";
    }
}
