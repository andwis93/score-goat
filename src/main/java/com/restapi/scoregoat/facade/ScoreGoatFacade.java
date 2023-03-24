package com.restapi.scoregoat.facade;

import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.domain.UserDto;
import com.restapi.scoregoat.domain.UserParam;
import com.restapi.scoregoat.domain.UserRespondDto;
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

    public UserRespondDto createUser(UserDto userDto) {
        return userService.signInUser(userDto);
    }

    public UserRespondDto tryLogIn(UserParam userParam) {
        return logInService.logInAttempt(userParam);
    }

    public Season fetchSeason(){
        return seasonService.getSeason();
    }

    public Season setSeason(int id){
        return seasonService.setSeason(id);
    }
}
