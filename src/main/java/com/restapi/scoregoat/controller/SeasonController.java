package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.domain.SeasonDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.mapper.SeasonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/scoregoat/season")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SeasonController {
    private final ScoreGoatFacade facade;
    private final SeasonMapper mapper;

    @GetMapping()
    public ResponseEntity<SeasonDto> getSeason() {
        return ResponseEntity.ok(mapper.mapSeasonToSeasonDto(facade.fetchSeason()));
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SeasonDto> setSeason(@RequestParam int id) {
        Season season = facade.setSeason(id);
        return ResponseEntity.ok(mapper.mapSeasonToSeasonDto(season));
    }
}
