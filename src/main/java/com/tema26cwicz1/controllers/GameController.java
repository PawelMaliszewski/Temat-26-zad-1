package com.tema26cwicz1.controllers;

import com.tema26cwicz1.Result.GameResult;
import com.tema26cwicz1.bet.Bet;
import com.tema26cwicz1.bet.BetGame;
import com.tema26cwicz1.game.Game;
import com.tema26cwicz1.game.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/game_list")
    public String gameList(Model model) {
        model.addAttribute("gameList", gameRepository.findAll());
        return "game_list";
    }

    @PostMapping("/delete_game")
    public String deleteGame(@RequestParam Long gameId, Model model) {
        if (gameId != null) {
            gameRepository.deleteById(gameId);
        }
        model.addAttribute("gameList", gameRepository.findAll());
        return "redirect:/game_list";
    }

    @PostMapping("/add_edit")
    public String editGame(@RequestParam(required = false) Long gameId, Model model) {
        if (gameId != null) {
            Optional<Game> optionalGame = gameRepository.findById(gameId);
            if (optionalGame.isPresent()) {
                Game game = optionalGame.get();
                model.addAttribute("game", game);
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
