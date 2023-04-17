package com.restapi.scoregoat.facade;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.mapper.MatchMapper;
import com.restapi.scoregoat.service.LogInService;
import com.restapi.scoregoat.service.MatchService;
import com.restapi.scoregoat.service.SeasonService;
import com.restapi.scoregoat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreGoatFacade {
    private final UserService userService;
    private final SeasonService seasonService;
    private final MatchService matchService;
    private final LogInService logInService;
    private final MatchMapper mapper;

    public Season fetchSeason(){
        return seasonService.fetchSeason();
    }

    public Season setSeason(){
        return seasonService.setSeason();
    }

    public UserRespondDto createUser(UserDto userDto) {
        return userService.signUpUser(userDto);
    }

    public UserRespondDto tryLogIn(UserParamDto userParam) {
        return logInService.logInAttempt(userParam);
    }

    public UserRespondDto changePassword(PasswordDto passwordDto) {
        return userService.changePassword(passwordDto);
    }

    public UserRespondDto accountChange(AccountDto accountDto) {
        return userService.accountChange(accountDto);
    }
    public MatchRespondDto uploadMatchesFromLeagueConfigList(){
        return matchService.uploadMatchesFromLeagueConfigList();
    }

    public List<MatchDto> findByLeagueIdOrderByDate(long userId, int leagueId) {

        return mapper.mapMatchToMatchDtoList(matchService.eliminateStarted(userId, leagueId));
    }
}
