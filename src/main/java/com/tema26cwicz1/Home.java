package com.tema26cwicz1;

import com.tema26cwicz1.appservice.AppService;
import com.tema26cwicz1.bet.Bet;
import com.tema26cwicz1.bet.BetGame;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {

    private final AppService appService;

    public Home(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Bet bet = new Bet();
        model.addAttribute("top4GamesList", appService.findTop4Bets());
        model.addAttribute("bet",  bet);
        model.addAttribute("ListOfAvailableGames", appService.gamesForBet());
        return "index";
    }
}
