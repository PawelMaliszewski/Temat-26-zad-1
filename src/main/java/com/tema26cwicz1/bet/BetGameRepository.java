package com.tema26cwicz1.bet;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetGameRepository extends JpaRepository<BetGame, Long> {


    List<BetGame> findBetGamesByBet(Bet bet);
}
