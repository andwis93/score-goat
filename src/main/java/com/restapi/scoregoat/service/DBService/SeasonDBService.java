package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.repository.SeasonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class SeasonDBService {

    private final SeasonRepository repository;

    public List<Season> findAll() {
        return repository.findAll();
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void save(Season season) {
        repository.save(season);
    }
}
