package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Ranking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Transactional
@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    List<Ranking> findByLeagueId(int id);
    Ranking findByUserIdAndLeagueId(Long userId, int leagueId);
    boolean existsRankingByUserIdAndLeagueId(Long userId, int leagueId);
}
