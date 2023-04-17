package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.MatchDto;
import com.restapi.scoregoat.domain.MatchRespondDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("v1/scoregoat/match")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MatchController {
    private final ScoreGoatFacade facade;

    @PostMapping
    public ResponseEntity<List<MatchDto>> getMatchesByLeagueId(@RequestParam Long userId, @RequestParam int leagueId) {
        return ResponseEntity.ok(facade.findByLeagueIdOrderByDate(userId, leagueId));
    }

    @PutMapping()
    public ResponseEntity<MatchRespondDto> uploadMatchesFromLeagueConfigList() {
        return ResponseEntity.ok(facade.uploadMatchesFromLeagueConfigList());
    }
}
