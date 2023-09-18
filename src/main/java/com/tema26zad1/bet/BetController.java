package com.tema26zad1.bet;

import com.tema26zad1.appservice.BetService;
import com.tema26zad1.appservice.GameService;

import com.tema26zad1.betgame.TempBetGame;
import jakarta.servlet.http.HttpSession;
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

    private final BetService betService;
    private final GameService gameService;
    private TemporaryBet temporaryBet;

    public BetController(BetService betService, GameService gameService, TemporaryBet temporaryBet) {
        this.betService = betService;
        this.gameService = gameService;
        this.temporaryBet = temporaryBet;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession httpSession) {
        if (!temporaryBet.isNotActive()) {
            model.addAttribute("betCouponGamesList", temporaryBet.getTempBetGames());
            List<Long> gameIdList = new ArrayList<>();
            for (TempBetGame tempBetGame1 : temporaryBet.getTempBetGames()) {
                gameIdList.add(tempBetGame1.getGameId());
            }

            model.addAttribute("listOfAvailableGames", gameService.onlyUnselectedGames(gameIdList));
        } else {
            model.addAttribute("listOfAvailableGames", gameService.gamesForBet());
        }
        model.addAttribute("bet", temporaryBet);
        model.addAttribute("top4GamesList", gameService.fourMostFrequentBetGames());
        model.addAttribute("betGame", new TempBetGame());
        return "index";
    }

    @PostMapping("/addBetGame")
    public String addBet(@ModelAttribute TempBetGame tempBetGame, @ModelAttribute TemporaryBet bet, HttpSession httpSession) {
        if (bet.getBetMoney() != null) {
            temporaryBet.setBetMoney(bet.getBetMoney());
        }
        if (tempBetGame.getGameResult() != null) {
            tempBetGame.setWinRate(betService.findWinRate(tempBetGame, null, gameService.findGameById(tempBetGame.getGameId())));
            temporaryBet.getTempBetGames().add(tempBetGame);
        }
        temporaryBet = betService.toWinTempBet(temporaryBet);
        return "redirect:/";
    }

    @GetMapping("/remove")
    public String remove(@ModelAttribute Bet bet, @RequestParam(required = false) Long betByGameId, HttpSession httpSession) {
        bet.getBetGames().removeIf(betGame -> betGame.getGameId().equals(betByGameId));
        if (!betService.findAllBetGames().isEmpty()) {
            bet = betService.toWin(bet);
        }
        return "redirect:/";
    }

    @GetMapping("/save_coupon")
    public String saveCoupon(HttpSession httpSession) {
        betService.saveBet(betService.convertToBetGame(temporaryBet));
        temporaryBet.clear();
        return "redirect:/";
    }

    @GetMapping("bet_list")
    public String betList(Model model, HttpSession httpSession) {
        model.addAttribute("betList", betService.findAllBets());
        System.out.println();
        return "bet_list";
    }
}
