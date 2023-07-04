package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Ranking;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Transactional
@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    Ranking findByLeagueIdAndUserId(int leagueId, Long userid);
    List<Ranking> findByLeagueId(int id);
    Ranking findByUserIdAndLeagueId(Long userId, int leaguId);
    boolean existsRankingByLeagueIdAndUserId(int leagueId, Long userId);
    int countByLeagueId(int leagueId);
}
