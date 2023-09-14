package com.tema26cwicz1.controllers;

import com.tema26cwicz1.appservice.AppService;
import com.tema26cwicz1.bet.Bet;
import com.tema26cwicz1.betgame.BetGame;
import com.tema26cwicz1.betgame.BetGameRepository;
import com.tema26cwicz1.bet.BetRepository;
import com.tema26cwicz1.game.GameRepository;
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
    private List<BetGame> temporaryBetGameList = new ArrayList<>();
    private Bet temporaryBet = new Bet();

    public BetController(AppService appService, BetGameRepository betGameRepository, BetRepository betRepository, GameRepository gameRepository) {
        this.appService = appService;
        this.betGameRepository = betGameRepository;
        this.betRepository = betRepository;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        if (!temporaryBetGameList.isEmpty()) {
            model.addAttribute("betCouponGamesList", temporaryBetGameList);
            model.addAttribute("listOfAvailableGames", appService.onlyUnselectedGames(temporaryBetGameList));
            model.addAttribute("toWin", (temporaryBet.getBetMoney() != null) ? appService.toWin(temporaryBet.getBetMoney(), temporaryBetGameList) : 0);
        } else {
            model.addAttribute("listOfAvailableGames", appService.gamesForBet(gameRepository.findAll()));
        }
        model.addAttribute("bet", temporaryBet);
        model.addAttribute("top4GamesList", appService.fourMostFrequentBetGames());
        System.out.println();
        return "index";
    }

    @PostMapping("/addBet")
    public String addBet(@ModelAttribute BetGame betGame, @ModelAttribute Bet bet) {
        if (bet.getBetMoney() != null) {
            temporaryBet.setBetMoney(bet.getBetMoney());
        }
        if (betGame.getGameResult() != null) {
            temporaryBetGameList.add(betGame);
            betGame.setBet(temporaryBet);
            betGame.setWinRate(appService.findWinRate(betGame, gameRepository.findById(betGame.getGameId()).get()));
        }
        System.out.println();
        return "redirect:/";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam(required = false) Long betByGameId) {
        temporaryBetGameList.removeIf(betGame -> betGame.getGameId().equals(betByGameId));
        return "redirect:/";
    }

    @GetMapping("/save_coupon")
    public String saveCoupon() {
        betRepository.save(temporaryBet);
        betGameRepository.saveAll(temporaryBetGameList);
        temporaryBet = new Bet();
        temporaryBetGameList = new ArrayList<>();
        return "redirect:/";
    }
}
