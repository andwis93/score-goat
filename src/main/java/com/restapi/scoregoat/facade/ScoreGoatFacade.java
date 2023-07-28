package com.restapi.scoregoat.facade;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.manager.MatchManager;
import com.restapi.scoregoat.service.ClientService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScoreGoatFacade {
    private final UserClientService userService;
    private final SeasonClientService seasonService;
    private final MatchClientService matchService;
    private final LogInClientService logInService;
    private final MatchPredictionClientService predictionService;
    private final RankingClientService rankingService;
    private final MatchManager manager;

    public Season fetchSeason(){
        return seasonService.fetchSeason();
    }

    public Season setSeason(){
        return seasonService.setSeason();
    }

    public UserRespondDto createUser(UserDto userDto) {
        return userService.signUpUser(userDto);
    }

    public UserRespondDto tryLogIn(UserDto userDto) {
        return logInService.logInAttempt(userDto);
    }

    public UserRespondDto changePassword(PasswordDto passwordDto) {
        return userService.changePassword(passwordDto);
    }

    public UserRespondDto accountChange(AccountDto accountDto) {
        return userService.accountChange(accountDto);
    }

    public void uploadMatchesFromLeagueConfigList(){
        matchService.uploadMatchesFromLeagueConfigList();
    }

    public List<Match> findByLeagueIdOrderByDate(long userId, int leagueId) {
        return matchService.eliminateSelected(userId, leagueId);
    }

    public NotificationRespondDto saveUserPredictions(PredictionDto predictionDto) {
        predictionDto.setMatchSelections(manager.removeEmptyPredictions(predictionDto.getMatchSelections()));
        return predictionService.savePredictions(predictionDto);
    }

    public List<UserPredictionDto> getUserMatchPredictions(Long userId, int leagueId) {
        return predictionService.getMatchPredictions(userId, leagueId);
    }

    public UserRespondDto deleteUser(UserDto userDto) {
        return userService.deleteUser(userDto);
    }

    public void rankPredictions() {
        predictionService.rankPredictions();
        predictionService.assignPoints();
    }

    public void assignRanks() {
       rankingService.executeRankAssign();
    }

    public List<RankingDto> fetchRankingDtoListByLeagueId(int leagueId) {
      return rankingService.fetchRankingListByLeagueId(leagueId);
    }

    public RankingDto fetchRankingDtoByUserIdAndLeagueId(Long userId, int leagueId) {
        return rankingService.fetchRankingByUserIdAndLeagueId(userId, leagueId);
    }

    public NotificationRespondDto resetPassword(String email) {
        return userService.resetPassword(email);
    }
}
