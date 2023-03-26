package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.PasswordDto;
import com.restapi.scoregoat.domain.UserDto;
import com.restapi.scoregoat.domain.UserRespondDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/scoregoat/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    private final ScoreGoatFacade facade;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRespondDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(facade.createUser(userDto));
    }

    @PostMapping(value = "/passwordchange" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRespondDto> changePassword(@RequestBody PasswordDto passwordDto){
       return ResponseEntity.ok(facade.changePassword(passwordDto));
    }
}
