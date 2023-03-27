package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.client.Season;
import com.restapi.scoregoat.domain.client.SeasonDto;
import org.springframework.stereotype.Service;

@Service
public class SeasonMapper {
    public SeasonDto mapSeasonToSeasonDto(final Season season) {
        return new SeasonDto(season.getYear());
    }
}
