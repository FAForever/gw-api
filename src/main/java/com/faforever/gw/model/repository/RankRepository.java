package com.faforever.gw.model.repository;

import com.faforever.gw.model.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<Rank, Integer> {
    @Query("SELECT r FROM Rank r WHERE r.xpMin > :xp AND r.level = :currentRank + 1")
    Optional<Rank> findNextRank(@Param("currentRank")Integer currentRank, @Param("xp")Long xp);

}
