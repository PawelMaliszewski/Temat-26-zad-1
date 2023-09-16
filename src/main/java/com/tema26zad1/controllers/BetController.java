package com.tema26zad1.controllers;

import com.tema26zad1.appservice.AppService;
import com.tema26zad1.bet.Bet;
import com.tema26zad1.betgame.BetGame;
import com.tema26zad1.betgame.BetGameRepository;
import com.tema26zad1.bet.BetRepository;
import com.tema26zad1.game.GameRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BetController {

    private final AppService appService;
    private final BetGameRepository betGameRepository;
    private final BetRepository betRepository;
    private final GameRepository gameRepository;
    @PersistenceContext
    public EntityManager entityManager;
    private Bet temporaryBet = new Bet();

    public BetController(AppService appService, BetGameRepository betGameRepository, BetRepository betRepository, GameRepository gameRepository) {
        this.appService = appService;
        this.betGameRepository = betGameRepository;
        this.betRepository = betRepository;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        if (!temporaryBet.getBetGames().isEmpty()) {
            model.addAttribute("betCouponGamesList", temporaryBet.getBetGames());
            model.addAttribute("listOfAvailableGames", appService.onlyUnselectedGames(temporaryBet.getBetGames()));
        } else {
            model.addAttribute("listOfAvailableGames", appService.gamesForBet(gameRepository.findAll()));
        }
        model.addAttribute("bet", temporaryBet);
        model.addAttribute("top4GamesList", appService.fourMostFrequentBetGames());
        model.addAttribute("betGame", new BetGame());
        return "index";
    }

    @PostMapping("/addBet")
    public String addBet(@ModelAttribute BetGame betGame, @ModelAttribute Bet bet) {
        if (bet.getBetMoney() != null) {
            temporaryBet.setBetMoney(bet.getBetMoney());
            temporaryBet = appService.toWin(temporaryBet);
        }
        if (betGame.getGameResult() != null) {
            temporaryBet.getBetGames().add(betGame);
            betGame.setBet(temporaryBet);
            betGame.setWinRate(appService.findWinRate(betGame, gameRepository.findById(betGame.getGameId()).get()));
            temporaryBet = appService.toWin(temporaryBet);
        }
        return "redirect:/";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam(required = false) Long betByGameId) {
        temporaryBet.getBetGames().removeIf(betGame -> betGame.getGameId().equals(betByGameId));
        if (!betGameRepository.findAll().isEmpty()) {
            temporaryBet = appService.toWin(temporaryBet);
        }
        return "redirect:/";
    }

    @GetMapping("/save_coupon")
    public String saveCoupon() {
        betRepository.save(temporaryBet);
        temporaryBet = new Bet();
        return "redirect:/";
    }

    @GetMapping("bet_list")
    public String betList(Model model) {
        model.addAttribute("betList", betRepository.findAll());
        System.out.println();
        return "bet_list";
    }
}
