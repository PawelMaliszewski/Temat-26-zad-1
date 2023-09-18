package com.tema26zad1.bet;

import com.tema26zad1.appservice.BetService;
import com.tema26zad1.appservice.GameService;

import com.tema26zad1.betgame.TempBetGame;
import com.tema26zad1.game.Game;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String home(@RequestParam(name = "thereAreEndedGames", required = false) String thereAreEndedGames, Model model, HttpSession httpSession) {
        System.out.println();
        if (!temporaryBet.getTempBetGames().isEmpty()) {
            model.addAttribute("betCouponGamesList", temporaryBet.getTempBetGames());
            List<Long> gameIdList = new ArrayList<>();
            for (TempBetGame tempBetGame1 : temporaryBet.getTempBetGames()) {
                gameIdList.add(tempBetGame1.getGameId());
            }
            model.addAttribute("listOfAvailableGames", gameService.onlyUnselectedGames(gameIdList));
        } else {
            model.addAttribute("listOfAvailableGames", gameService.gamesForBet());
            System.out.println();
        }
        model.addAttribute("thereAreEndedGames", thereAreEndedGames);
        model.addAttribute("bet", temporaryBet);
        model.addAttribute("top4GamesList", gameService.fourMostFrequentBetGames());
        model.addAttribute("betGame", new TempBetGame());
        return "index";
    }

    @GetMapping("/save_coupon")
    public String saveCoupon(RedirectAttributes ra, Model model, HttpSession httpSession) {
        List<Game> listOfEndedGames = gameService.listOfGamesThatAreEnded(temporaryBet);
        if (listOfEndedGames.isEmpty()) {
            Bet bet = betService.convertToBetGame(temporaryBet);
            betService.saveBet(bet);
            temporaryBet.clear();
            model.addAttribute("bet", bet);
            return "bet_added";
        } else {
            for (Game game : listOfEndedGames) {
                temporaryBet.getTempBetGames().removeIf(tempBetGame -> tempBetGame.getGameId().equals(game.getGameId()));
            }
            String thereAreEndedGames = "Nie udało sie dodać zakładu niektóre gry sie skończyły";
            ra.addAttribute("thereAreEndedGames", thereAreEndedGames);
        }
        return "redirect:/";
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
    public String remove(@ModelAttribute TempBetGame bet, @RequestParam(required = false) Long betByGameId, HttpSession httpSession) {
        temporaryBet.getTempBetGames().removeIf(betGame -> betGame.getGameId().equals(betByGameId));
        if (!betService.findAllBetGames().isEmpty()) {
            temporaryBet = betService.toWinTempBet(temporaryBet);
        }
        return "redirect:/";
    }



    @GetMapping("bet_list")
    public String betList(Model model, HttpSession httpSession) {
        model.addAttribute("betList", betService.findAllBets());
        System.out.println();
        return "bet_list";
    }
}
