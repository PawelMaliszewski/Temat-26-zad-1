package com.tema26cwicz1.controllers;

import com.tema26cwicz1.appservice.AppService;
import com.tema26cwicz1.bet.Bet;
import com.tema26cwicz1.bet.BetGame;
import com.tema26cwicz1.bet.BetGameRepository;
import com.tema26cwicz1.bet.BetRepository;
import com.tema26cwicz1.game.Game;
import com.tema26cwicz1.game.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class BetController {

    private final AppService appService;
    private final BetGameRepository betGameRepository;
    private final BetRepository betRepository;
    private final GameRepository gameRepository;

    public BetController(AppService appService, BetGameRepository betGameRepository, BetRepository betRepository, GameRepository gameRepository) {
        this.appService = appService;
        this.betGameRepository = betGameRepository;
        this.betRepository = betRepository;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/addBet")
    public String addBet(@ModelAttribute() Bet bet, @ModelAttribute BetGame betGame, RedirectAttributes ra) {
        if (bet.getBetId() == null) {
            bet = new Bet();
            betRepository.save(bet);
        }

        if (betGame.getGameId() != null) {
            betGame.setBet(bet);
            betGame.setWinRate(appService.findWinRate(betGame, gameRepository.findById(betGame.getGameId()).get()));
            betGameRepository.save(betGame);
        }
        ra.addFlashAttribute("bet", bet);
        ra.addFlashAttribute("betCouponGamesList", betGameRepository.findBetGamesByBet(bet));
        System.out.println();
        return "redirect:/";
    }

    @Transactional
    @GetMapping("/remove")
    public String remove(@RequestParam(required = false) Long bet_game_id, @RequestParam Long bet_id, RedirectAttributes ra) {
        if (bet_game_id == null && bet_id != null) {
            betRepository.deleteById(bet_id);
        } else if (bet_game_id != null && bet_id != null) {
            betGameRepository.deleteById(bet_game_id);
            Bet bet = new Bet();
            bet.setBetId(bet_id);
            ra.addAttribute("bet", bet);
        }
        System.out.println();
        return "redirect:/";
    }
}
