package com.tema26zad1.game;

import com.tema26zad1.appservice.BetService;
import com.tema26zad1.appservice.GameService;
import com.tema26zad1.appservice.GameBetFacade;
import com.tema26zad1.betgame.BetGame;
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

    private final GameBetFacade gameBetFacade;
    private final BetService betService;
    private final GameService gameService;

    public GameController(GameBetFacade gameBetFacade, BetService betService, GameService gameService) {
        this.gameBetFacade = gameBetFacade;
        this.betService = betService;
        this.gameService = gameService;
    }

    @GetMapping("/game_list")
    public String gameList(Model model) {
        model.addAttribute("gameList", gameService.findAllGames());
        return "game_list";
    }

    @PostMapping("/delete_game")
    public String deleteGame(@RequestParam Long gameId, @RequestParam(required = false) String yes, RedirectAttributes ra) {
        if (gameId != null) {
            List<BetGame> betGamesByGameId = betService.findAllByGameId(gameId);
            if (betGamesByGameId.isEmpty()) {
                gameService.deleteGameById(gameId);
                return "redirect:/game_list";
            } else {
                boolean isThereAnyActiveBetForThisGame = betGamesByGameId.stream().noneMatch(betGame -> betGame.getBet().isNotActive());
                if (!isThereAnyActiveBetForThisGame) {
                    gameService.deleteGameById(gameId);
                    return "redirect:/game_list";
                } else {
                    if (yes == null) {
                        ra.addAttribute("gameId", gameId);
                        return "redirect:warning";
                    } else {
                        gameBetFacade.deleteGameAndBetAndBetGamesRelatedToGameId(gameId, betGamesByGameId);
                        return "redirect:/game_list";
                    }
                }
            }
        }
        return "index";
    }

    @GetMapping("/warning")
    private String test(@RequestParam Long gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "warning";
    }

    @GetMapping("/game/add")
    public String editGame(Model model) {
        model.addAttribute("game", new Game());
        return "/add_edit_match";
    }

    @GetMapping("/game/edit")
    public String editGame(@RequestParam(required = false, name = "game_id") Long gameId, Model model) {
        Optional<Game> optionalGame = getOptionalGameByGameId(gameId);
        if (optionalGame.isPresent()) {
            model.addAttribute("game", optionalGame.get());
        } else {
            return "error";
        }
        return "add_edit_match";
    }

    @PostMapping("/save_game")
    public String saveGame(@ModelAttribute Game game, Model model) {
        gameBetFacade.saveAndUpdateBetsThatAreContainThisGame(game);
        model.addAttribute("gameList", gameService.findAllGames());
        return "redirect:/game_list";
    }

    @PostMapping("/random_result")
    public String randomResult(@RequestParam Long gameId, Model model) {
        if (gameId != null ) {
            Optional<Game> optionalGame = getOptionalGameByGameId(gameId);
            Random random = new Random();
            GameResult gameResult = GameResult.values()[random.nextInt(0, 3)];
            if (optionalGame.isPresent()) {
                Game game = optionalGame.get();
                game.setGameResult(gameResult);
                gameBetFacade.saveAndUpdateBetsThatAreContainThisGame(game);
            }
        }
        model.addAttribute("gameList", gameService.findAllGames());
        return "redirect:/game_list";
    }

    private Optional<Game> getOptionalGameByGameId(Long gameId) {
        return gameService.findGameById(gameId);
    }
}
