package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.*;
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

    @GetMapping(value = "/verify")
    public ResponseEntity<UserRespondDto> verifyUser(@RequestParam String userName, @RequestParam String email) {
        return ResponseEntity.ok(facade.verifyUser(userName, email));
    }

    @PutMapping(value = "/passwordchange" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRespondDto> changePassword(@RequestBody PasswordDto passwordDto){
        return ResponseEntity.ok(facade.changePassword(passwordDto));
    }

    @PutMapping(value = "/accountchange", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRespondDto> accountChange(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(facade.accountChange(accountDto));
    }

    @PutMapping(value = "/passwordreset")
    public ResponseEntity<NotificationRespondDto> resetPassword(@RequestParam String emailOrName){
        return ResponseEntity.ok(facade.resetPassword(emailOrName));
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRespondDto> deleteUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(facade.deleteUser(userDto));
    }

    @GetMapping
    public ResponseEntity<String> provideVerificationCode(@RequestParam String email){
        return ResponseEntity.ok(facade.generateVerificationCode(email));
    }
}
