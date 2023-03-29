package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.domain.client.mapJSON.Year;
import org.springframework.stereotype.Service;

@Service
public class SeasonMapper {
    public Year mapSeasonToSeasonDto(final Season season) {
        return new Year(season.getYear());
    }
}
