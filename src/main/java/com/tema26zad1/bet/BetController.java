package com.tema26zad1.bet;

import com.tema26zad1.appservice.BetService;
import com.tema26zad1.appservice.GameService;
import com.tema26zad1.game.Game;
import com.tema26zad1.game.GameDto;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BetController {

    private final BetService betService;
    private final GameService gameService;

    private final ModelMapper modelMapper;

    public BetController(BetService betService, GameService gameService, ModelMapper modelMapper) {
        this.betService = betService;
        this.gameService = gameService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("listOfAvailableGames", gameService.gamesForBet().stream()
                .map(this::convertToDto).collect(Collectors.toList()));
        model.addAttribute("top4GamesList", gameService.fourMostFrequentBetGames());
        return "index";
    }

    @GetMapping({"/bet", "/find_bet"})
    public String betAdded(@RequestParam(required = false) Long betId, Model model) {
        if (betId != null) {
            Optional<Bet> betById = betService.getBetRepository().findById(betId);
            betById.ifPresentOrElse(bet -> model.addAttribute("bet", bet),
                    () -> model.addAttribute("notFound", "Nie znaleziono zakładu o Id: " + betId));
        }
        return "bet";
    }

    @GetMapping("bet_list")
    public String betList(@NotNull Model model) {
        model.addAttribute("betList", betService.findAllBets());
        return "bet_list";
    }

    @PostMapping(value = "/bet")
    public ResponseEntity<Bet> update(@RequestBody Bet bet) {
        if (gameService.checkIfAnyGameEnded(bet)) {
            return ResponseEntity.badRequest().build();
        } else {
            betService.saveBetAndSetValues(bet);
            bet.getBetGames().clear();
            return ResponseEntity.ok(bet);
        }
    }

    private GameDto convertToDto(Game game) {
        return modelMapper.map(game, GameDto.class);
    }
}
