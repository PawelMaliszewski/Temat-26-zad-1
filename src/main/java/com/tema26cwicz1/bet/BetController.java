package com.tema26cwicz1.bet;

import com.tema26cwicz1.game.Game;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BetController {

    private final BetRepository betRepository;

    public BetController(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @PostMapping("/addBet")
    public String addBet(@ModelAttribute Game game, @ModelAttribute Bet bet, Model model) {
        model.addAttribute("bet", bet);
        System.out.println("s");
        return "index";
    }
}
