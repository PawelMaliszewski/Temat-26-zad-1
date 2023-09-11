package com.tema26cwicz1.controllers;

import com.tema26cwicz1.appservice.AppService;
import com.tema26cwicz1.bet.Bet;
import com.tema26cwicz1.bet.BetGame;
import com.tema26cwicz1.bet.BetGameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BetController {

    private final AppService appService;
    private final BetGameRepository betGameRepository;

    public BetController(AppService appService, BetGameRepository betGameRepository) {
        this.appService = appService;
        this.betGameRepository = betGameRepository;
    }

    @PostMapping("/addBet")
    public String addBet(@ModelAttribute() Bet bet, @ModelAttribute BetGame betGame, RedirectAttributes ra) {
        if (bet.getBetId() == null) {
            bet = new Bet();
            appService.addBet(bet);
        }
        betGame.setBet(bet);
        betGameRepository.save(betGame);
        ra.addFlashAttribute("bet", bet);
        ra.addFlashAttribute("betCouponGamesList", betGameRepository.findBetGamesByBet(bet));
        System.out.println();
        return "redirect:/";
    }
}
