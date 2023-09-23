package com.tema26zad1.bet;

import com.tema26zad1.appservice.BetService;
import com.tema26zad1.appservice.GameService;
import com.tema26zad1.betgame.BetGame;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BetController {

    private final BetService betService;
    private final GameService gameService;

    public BetController(BetService betService, GameService gameService) {
        this.betService = betService;
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("listOfAvailableGames", gameService.gamesForBet());
        model.addAttribute("top4GamesList", gameService.fourMostFrequentBetGames());
        return "index";
    }

    @GetMapping({"/bet", "/find_bet"})
    public String betAdded(@RequestParam(required = false) Long betId, Model model) {
        if (betId != null) {
            Optional<Bet> betById = betService.getBetRepository().findById(betId);
            betById.ifPresentOrElse(bet -> model.addAttribute("bet", bet),
                    () -> model.addAttribute("notFound", "Nie znaleziono zakładu o Id:" + ((betId != null) ? betId : "")));
        }
        return "bet";
    }

    @GetMapping("bet_list")
    public String betList(@NotNull Model model) {
        model.addAttribute("betList", betService.findAllBets());
        return "bet_list";
    }

    @PostMapping(value = "/bet")
    public ResponseEntity<Bet> update(@RequestBody Bet bet) {
        System.out.println(bet.toString());
        List<BetGame> tempBetGames = new ArrayList<>(bet.getBetGames());
        bet.getBetGames().clear();
        betService.saveBet(bet);
        tempBetGames.forEach(betGame -> betGame.setBet(bet));
        betService.getBetGameRepository().saveAll(tempBetGames);
        return ResponseEntity.ok(bet);
    }

}
