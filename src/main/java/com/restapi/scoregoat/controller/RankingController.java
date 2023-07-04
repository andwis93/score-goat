package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.RankingDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.mapper.RankingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/scoregoat/ranking")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RankingController {
    private final ScoreGoatFacade facade;
    private final RankingMapper mapper;

    @GetMapping
    public ResponseEntity<List<RankingDto>> getRankingListByLeagueId(@RequestParam int leagueId) {
        return ResponseEntity.ok(mapper.mapRankingToRankingDtoList(facade.fetchRankingDtoListByLeagueId(leagueId)));
    }

    @GetMapping("/single")
    public ResponseEntity<RankingDto> getRankingByUserIdAndLeagueId(@RequestParam Long userId, @RequestParam int leagueId) {
        return ResponseEntity.ok(mapper.mapRankingToRankingDto(facade.fetchRankingDtoByUserIdAndLeagueId(userId, leagueId)));
    }
}
