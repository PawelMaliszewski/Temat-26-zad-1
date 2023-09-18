package com.tema26zad1.betgame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BetGameRepository extends JpaRepository<BetGame, Long> {

    List<BetGame> findAllByBet_BetId(Long gameId);

    List<BetGame> findAllByGameId(Long gameId);
}
