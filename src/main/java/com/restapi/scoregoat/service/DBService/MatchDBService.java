package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.Match;
import com.restapi.scoregoat.repository.MatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class MatchDBService {
    private final MatchRepository repository;

    public Match findMatchByFixture(Long fixtureId) {
        if (repository.existsByFixtureId(fixtureId)) {
            return repository.findByFixtureId(fixtureId).orElseThrow(NoSuchElementException::new);
        } else {
            return null;
        }
    }

    public List<Match> findByLeagueIdOrderByDate(int leagueId) {
        return repository.findByLeagueIdOrderByDate(leagueId);
    }

    public void deleteFixtures(){
        repository.deleteAll();
    }

    public void saveAll(List<Match> list){
        repository.saveAll(list);
    }
}
