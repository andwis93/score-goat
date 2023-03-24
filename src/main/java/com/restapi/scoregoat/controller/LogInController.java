package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.UserParam;
import com.restapi.scoregoat.domain.UserRespondDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/scoregoat/login")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LogInController {
    private final ScoreGoatFacade facade;

    @GetMapping()
    public UserRespondDto logUserIn(@RequestParam String name, @RequestParam String password) {
        return facade.tryLogIn(new UserParam(name, password));
    }
}
