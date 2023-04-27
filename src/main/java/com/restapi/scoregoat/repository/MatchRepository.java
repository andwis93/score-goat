package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByLeagueIdOrderByDate(int leagueId);
    Optional<Match> findByFixtureId(long fixtureId);
    Boolean existsByFixtureId(long fixtureId);

}
