package com.tema26zad1.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "SELECT g FROM Game g WHERE g.gameResult = 'WAITING'")
    List<Game> findAllGamesThatAreNotEnded();
}
