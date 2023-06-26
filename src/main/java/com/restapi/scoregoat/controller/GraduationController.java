package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.GraduationDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.mapper.GraduationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/scoregoat/graduation")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GraduationController {
    private final ScoreGoatFacade facade;
    private final GraduationMapper mapper;

    @GetMapping
    public ResponseEntity<List<GraduationDto>> getGraduationListByLeagueId(@RequestParam int leagueId) {
        return ResponseEntity.ok(mapper.mapGraduationToGraduationDtoList(facade.fetchGraduationDtoListByLeagueId(leagueId)));
    }
}
