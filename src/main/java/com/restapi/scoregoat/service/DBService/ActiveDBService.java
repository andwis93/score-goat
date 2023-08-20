package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.Active;
import com.restapi.scoregoat.repository.ActiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ActiveDBService {
    private final ActiveRepository repository;
    public Active findByLeagueId(int leagueId) {
        return repository.findByLeagueId(leagueId);
    }
    public void save(Active active) {
        repository.save(active);
    }
}
