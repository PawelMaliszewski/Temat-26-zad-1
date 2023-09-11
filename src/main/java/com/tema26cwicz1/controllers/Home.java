package com.tema26cwicz1.controllers;

import com.tema26cwicz1.appservice.AppService;
import com.tema26cwicz1.bet.Bet;
import com.tema26cwicz1.bet.BetGame;
import com.tema26cwicz1.bet.BetGameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class Home {

    private final AppService appService;

    private final BetGameRepository betGameRepository;

    public Home(AppService appService, BetGameRepository betGameRepository) {
        this.appService = appService;
        this.betGameRepository = betGameRepository;
    }

    @GetMapping("/")
    public String home(@ModelAttribute Bet bet, Model model) {
        model.addAttribute("betGame", new BetGame());
        model.addAttribute("top4GamesList", appService.fourMostFrequentBetGames());
        model.addAttribute("listOfAvailableGames", appService.gamesForBet());
        if (bet.getBetId() != null) {
            model.addAttribute("betCouponGamesList", betGameRepository.findBetGamesByBet(bet));
        }
        model.addAttribute("bet", bet);
        System.out.println();
        return "index";
    }
}
