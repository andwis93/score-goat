package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.MatchPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, Long> {
    boolean existsMatchPredictionByUserIdAndMatchId(Long userId, Long machId);

}
