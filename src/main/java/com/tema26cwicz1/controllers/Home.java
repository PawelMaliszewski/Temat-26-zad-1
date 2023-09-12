package com.tema26cwicz1.controllers;

import com.tema26cwicz1.appservice.AppService;
import com.tema26cwicz1.bet.Bet;
import com.tema26cwicz1.bet.BetGame;
import com.tema26cwicz1.bet.BetGameRepository;
import com.tema26cwicz1.game.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Home {

    private final AppService appService;

    private final BetGameRepository betGameRepository;

    private final GameRepository gameRepository;

    public Home(AppService appService, BetGameRepository betGameRepository, GameRepository gameRepository) {
        this.appService = appService;
        this.betGameRepository = betGameRepository;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/")
    public String home(@ModelAttribute Bet bet, Model model) {
        model.addAttribute("betGame", new BetGame());
        model.addAttribute("top4GamesList", appService.fourMostFrequentBetGames());

        if (bet.getBetId() != null) {
            model.addAttribute("betCouponGamesList", betGameRepository.findBetGamesByBet(bet));
            model.addAttribute("listOfAvailableGames", appService.onlyUnselectedGames(bet.getBetId()));
            model.addAttribute("toWin", (bet.getBetMoney() != null) ? appService.toWin(bet, bet.getBetMoney()) : 0);
        } else {
            model.addAttribute("listOfAvailableGames", appService.gamesForBet(gameRepository.findAll()));
        }
        model.addAttribute("bet", bet);
        System.out.println();
        return "index";
    }
}
