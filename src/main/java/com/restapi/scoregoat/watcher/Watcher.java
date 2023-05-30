package com.restapi.scoregoat.watcher;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.service.LogDataService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Aspect
@Component
public class Watcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Watcher.class);
    private final LogDataService logDataService;

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.SeasonService.setSeason(..))"
            + "&& args(id) && target(object)", returning ="season", argNames = "id,object,season")
    public void logSetSeason(int id, Object object, Season season) {
        String message = "Set Season " + season.getYear() + " -- Class: " + object.getClass().getName();
        logDataService.saveLog(new LogData(null,"League ID: " + id, Code.SEASON_SET.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.UserService.signUpUser(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logSetCreateUser(UserRespondDto respond, Object object) {
        String message = "Create User with ID: " + respond.getId() + " with respond: " + respond.getRespond()
                + " -- Class: " + object;
        logDataService.saveLog(new LogData(respond.getId(),null, Code.USER_CREATE.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.LogInService.logInAttempt(..))"
            + "&&target(object)", returning = "respond", argNames = "respond,object")
    public void logLogIn(UserRespondDto respond, Object object) {
        String message = "Attempt to Log In User with ID: " + respond.getId() + " with respond: " + respond.getRespond()
                + " -- Class: " + object;
        logDataService.saveLog(new LogData(respond.getId(), null, Code.USER_LOGIN.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.UpdateLogInService.updateLogInLockedDates(..))"
            + "&&target(object)", returning = "respond", argNames = "respond,object")
    public void logUpdateLogInLockedDate(long respond, Object object) {
        String message = "Clean up locked LogIns in amount of: " + respond + " -- Class: " + object;
        logDataService.saveLog(new LogData(null, "Qty removed: " + respond,
                Code.LOGIN_LOCKED_DATES_UPDATE.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.SessionService.removeExpiredSession(..))"
           + "&&target(object)", returning = "respond", argNames = "respond,object")
    public void logExpiredSessionRemove(long respond, Object object) {
        String message = "Expired Sessions were removed in amount of: " + respond + " -- Class: " + object;
        logDataService.saveLog(new LogData(null, "Qty removed: " + respond,
                Code.SESSION_EXPIRED_REMOVE.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.UserService.accountChange(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void accountChange(UserRespondDto respond, Object object) {
        String message = "Attempt to change account Information for user with ID: " + respond.getId() + " with respond: "
                + respond.getRespond() + " -- Class: " + object;
        logDataService.saveLog(new LogData(respond.getId(), null, Code.USER_ACCOUNT_INFO_CHSNGE.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.UserService.changePassword(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logChangingPassword(UserRespondDto respond, Object object) {
        String message = "Attempt to change password for user with ID: " + respond.getId() + " with respond: "
                + respond.getRespond() + " -- Class: " + object;
        logDataService.saveLog(new LogData(respond.getId(), null, Code.USER_PASSWORD_CHANGED.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.UserService.deleteUser(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logDeleteUser(UserRespondDto respond, Object object) {
        String message = "Attempt to delete user for user with ID: " + respond.getId() + " with respond: "
                + respond.getRespond() + " -- Class: " + object;
        logDataService.saveLog(new LogData(respond.getId(), null, Code.USER_DELETE.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.MatchService.uploadMatches(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logUploadMatches(MatchRespondDto respond, Object object) {
        String message = "Attempt to upload matches for League ID: " + respond.getLeagueId() + " with respond: "
                + respond.getRespond() + " -- Class: " + object;
        logDataService.saveLog(new LogData((long) respond.getLeagueId(), null, Code.UPLOAD_MATCHES_OK.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.MatchService.uploadMatchesFromLeagueConfigList(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logUploadAllMatches(MatchRespondDto respond, Object object) {
        String message = "Attempt to upload all matches with respond: "
                + respond.getRespond() + " -- Class: " + object;
        logDataService.saveLog(new LogData(null, "LeagueConfigList", Code.UPLOAD_ALL_MATCHES_OK.getCode(), message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.MatchPredictionService.savePredictions(..))"
            + "&&args(predictionDto) &&target(object)" ,returning = "respond", argNames = "predictionDto, respond, object")
    public void saveUserPredictions(PredictionDto predictionDto, String respond, Object object) {
        String message = "Attempt to save all matches predictions by user: " + predictionDto.getUserId() + " with respond: "
                + respond + " -- Class: " + object;
        logDataService.saveLog(new LogData(null, "LeagueConfigList", Code.SAVE_ALL_PREDICTIONS.getCode(), message));
        LOGGER.info(message);
    }
}
