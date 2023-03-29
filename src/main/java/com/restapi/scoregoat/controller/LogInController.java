package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.UserParamDto;
import com.restapi.scoregoat.domain.UserRespondDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/scoregoat/login")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LogInController {
    private final ScoreGoatFacade facade;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRespondDto> logUserIn(@RequestBody UserParamDto userParam) {
        return ResponseEntity.ok(facade.tryLogIn(userParam));
    }
}
