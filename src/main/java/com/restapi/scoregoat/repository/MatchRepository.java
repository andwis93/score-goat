package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
    List<Match> findByLeagueId(int leagueId);
}
