package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.NotificationRespondDto;
import com.restapi.scoregoat.domain.PredictionDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/scoregoat/prediction")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MatchPredictionController {
    private final ScoreGoatFacade facade;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationRespondDto> savePrediction(@RequestBody PredictionDto predictionDto) {
        return ResponseEntity.ok(facade.saveUserPredictions(predictionDto));
    }
}
