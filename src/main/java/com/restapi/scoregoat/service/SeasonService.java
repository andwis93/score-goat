package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.repository.SeasonRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class SeasonService {
    private SeasonRepository repository;
    private final FootballClient footballClient;

    public Season setSeason(final int id) {
        Season season = new Season(footballClient.getFootballSeason(id));
        repository.deleteAll();
        repository.save(season);
        return season;
    }

    public Season getSeason() {
        List<Season> list = repository.findAll();
        return list.get(0);
    }
}
