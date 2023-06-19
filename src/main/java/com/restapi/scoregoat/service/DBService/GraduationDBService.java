package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.Graduation;
import com.restapi.scoregoat.repository.GraduationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class GraduationDBService {

    private final GraduationRepository repository;

    public List<Graduation> findByLeagueId(int leagueId) {
        return repository.findByLeagueId(leagueId);
    }

    public void save(Graduation graduation) {
        repository.save(graduation);
    }
    public void saveAll(List<Graduation> list) {
        repository.saveAll(list);
    }

    public boolean existsGraduationByLeagueAndUserId(int leagueId, long userId) {
        return repository.existsGraduationByLeagueIdAndUserId(leagueId, userId);
    }

    public Graduation findByLeagueAndUserId(int leagueId, long userId) {
        return repository.findByLeagueIdAndUserId(leagueId, userId);
    }
}
