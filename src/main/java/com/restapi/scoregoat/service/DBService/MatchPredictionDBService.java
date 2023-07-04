package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.MatchPrediction;
import com.restapi.scoregoat.repository.MatchPredictionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class MatchPredictionDBService {
    private final MatchPredictionRepository repository;

    public boolean existsMatchPredictionByUserIdAndFixtureId(Long userId, Long fixtureId ) {
       return repository.existsMatchPredictionByUserIdAndFixtureId(userId, fixtureId);
    }

    public void save(MatchPrediction prediction) {
        repository.save(prediction);
    }
    public void saveAll(List<MatchPrediction> predictions) {
        repository.saveAll(predictions);
    }

    public List<MatchPrediction> findByUserIdAndLeagueId(Long userId, int leagueId) {
        return repository.findByUserIdAndLeagueId(userId, leagueId);
    }

    public List<MatchPrediction> findAllByResult(String result) {
        return repository.findAllByResult(result);
    }

    public List<MatchPrediction> findAllByPoints(int points) {
        return repository.findAllByPoints(points);
    }
}
