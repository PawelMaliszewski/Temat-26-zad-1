package com.tema26zad1.betgame;

import com.tema26zad1.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BetGameRepository extends JpaRepository<BetGame, Long> {

    List<BetGame> findAllByGameId(Long gameId);

    @Query(value = "SELECT bg.gameId  FROM BetGame bg, Game g WHERE bg.gameId = g.gameId AND g.gameResult = 'WAITING'")
    List<Long> findAllGameIDsThatAreNotEndedBasedOnBetGames();
}
