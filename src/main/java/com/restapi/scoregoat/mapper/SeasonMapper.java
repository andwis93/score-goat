package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.domain.SeasonDto;
import org.springframework.stereotype.Service;

@Service
public class SeasonMapper {
    public SeasonDto mapSeasonToSeasonDbDto(Season season) {
        return new SeasonDto(season.getYear());
    }
}
