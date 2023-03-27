package com.restapi.scoregoat.facade;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.client.Season;
import com.restapi.scoregoat.service.LogInService;
import com.restapi.scoregoat.service.SeasonService;
import com.restapi.scoregoat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreGoatFacade {
    private final UserService userService;
    private final SeasonService seasonService;
    private final LogInService logInService;

    public Season fetchSeason(){
        return seasonService.getSeason();
    }

    public Season setSeason(int id){
        return seasonService.setSeason(id);
    }

    public UserRespondDto createUser(UserDto userDto) {
        return userService.signInUser(userDto);
    }

    public UserRespondDto tryLogIn(UserParamDto userParam) {
        return logInService.logInAttempt(userParam);
    }

    public UserRespondDto changePassword(PasswordDto passwordDto) {
        return userService.changePassword(passwordDto);
    }
}
