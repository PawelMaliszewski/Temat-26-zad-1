package com.tema26zad1.game;

import com.tema26zad1.appservice.BetService;
import com.tema26zad1.appservice.GameService;
import com.tema26zad1.appservice.AppService;
import com.tema26zad1.betgame.BetGame;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Random;

@Controller
public class GameController {

    private final AppService appService;

    private final BetService betService;

    private final GameService gameService;

    public GameController(AppService appService, BetService betService, GameService gameService) {
        this.appService = appService;
        this.betService = betService;
        this.gameService = gameService;
    }

    @GetMapping("/game_list")
    public String gameList(Model model) {
        model.addAttribute("gameList", gameService.findAllGames());
        return "game_list";
    }

    @PostMapping("/delete_game")
    public String deleteGame(@RequestParam Long gameId, @RequestParam(required = false) String yes, RedirectAttributes ra, HttpSession httpSession) {
        if (gameId != null) {
            List<BetGame> betGamesByGameId = betService.findAllByGameId(gameId);
            boolean isThereAnyActiveBetForThisGame = betGamesByGameId.stream().noneMatch(betGame -> betGame.getBet().isNotActive());
            if (!isThereAnyActiveBetForThisGame) {
                gameService.deleteGameById(gameId);
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
        return "index";
    }

    @GetMapping("/warning")
    private String test(@RequestParam Long gameId, Model model, HttpSession httpSession) {
        model.addAttribute("gameId", gameId);
        return "warning";
    }

    @GetMapping("/game/add")
    public String editGame(Model model, HttpSession httpSession) {
        model.addAttribute("game", new Game());
        return "/add_edit_match";
    }

    @GetMapping("/game/edit")
    public String editGame(@RequestParam(required = false, name = "game_id") Long gameId, Model model, HttpSession httpSession) {
        if (gameId != null) {
            model.addAttribute("game", gameService.findGameById(gameId));
        } else {
            return "error";
        }
        return "add_edit_match";
    }

    @PostMapping("/save_game")
    public String saveGame(@ModelAttribute Game game, Model model) {
        gameService.saveGame(game);
        betService.updateBets(game);
        model.addAttribute("gameList", gameService.findAllGames());
        return "redirect:/game_list";
    }

    @PostMapping("/random_result")
    public String randomResult(@RequestParam Long gameId, Model model) {
        if (gameId != null) {
            Game game = gameService.findGameById(gameId);
            Random random = new Random();
            GameResult gameResult = GameResult.values()[random.nextInt(0, 3)];
            if (game != null) {
                game.setGameResult(gameResult);
                gameService.saveGame(game);
                betService.updateBets(game);
            }
        }
        model.addAttribute("gameList", gameService.findAllGames());
        return "redirect:/game_list";
    }
}
