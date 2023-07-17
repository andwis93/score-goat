package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.Ranking;
import com.restapi.scoregoat.repository.RankingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class RankingDBService {

    private final RankingRepository repository;

    public List<Ranking> findByLeagueId(int leagueId) {
        return repository.findByLeagueId(leagueId);
    }
    public Ranking findByUserIdAndLeagueId(Long userId, int leagueId) {
        return repository.findByUserIdAndLeagueId(userId, leagueId);
    }
    public void save(Ranking ranking) {
        repository.save(ranking);
    }
    public void saveAll(List<Ranking> list) {
        repository.saveAll(list);
    }
    public boolean existsRankingByUserIdAndLeagueId(long userId, int leagueId) {
        return repository.existsRankingByUserIdAndLeagueId(userId, leagueId);
    }
}
