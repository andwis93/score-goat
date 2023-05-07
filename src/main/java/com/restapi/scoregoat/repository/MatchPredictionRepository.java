package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.MatchPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, Long> {
    boolean existsMatchPredictionByUserIdAndFixtureId(Long userId, Long fixtureId);
    List<MatchPrediction> findAllByUserIdAndLeagueId(Long userId, int leagueId);
  //  List<MatchPrediction> findAllByUserId(Long userId);
}
