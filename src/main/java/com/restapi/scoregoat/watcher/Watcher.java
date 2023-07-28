package com.restapi.scoregoat.watcher;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.service.DBService.LogDataDBService;
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
    private final LogDataDBService logDataService;

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.SeasonClientService.setSeason(..))"
            + "&& args(id) && target(object)", returning ="season", argNames = "id,object,season")
    public void logSetSeason(int id, Object object, Season season) {
        StringBuilder message = new StringBuilder("Set Season " + season.getYear() + " -- Class: " + object.getClass().getName());
        logDataService.saveLog(new LogData(null,"League ID: " + id, Code.SEASON_SET.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.UserClientService.signUpUser(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logSetCreateUser(UserRespondDto respond, Object object) {
        StringBuilder message = new StringBuilder("Create User with ID: " + respond.getId() + " with respond: " + respond.getRespond()
                + " -- Class: " + object);
        logDataService.saveLog(new LogData(respond.getId(),null, Code.USER_CREATE.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.LogInClientService.logInAttempt(..))"
            + "&&target(object)", returning = "respond", argNames = "respond,object")
    public void logLogIn(UserRespondDto respond, Object object) {
        StringBuilder message = new StringBuilder("Attempt to Log In User with ID: " + respond.getId() + " with respond: " + respond.getRespond()
                + " -- Class: " + object);
        logDataService.saveLog(new LogData(respond.getId(), null, Code.USER_LOGIN.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.LogInClientService.updateLogInLockedDates(..))"
            + "&&target(object)", returning = "respond", argNames = "respond,object")
    public void logUpdateLogInLockedDate(long respond, Object object) {
        StringBuilder message = new StringBuilder("Clean up locked LogIns in amount of: " + respond + " -- Class: " + object);
        logDataService.saveLog(new LogData(null, "Qty removed: " + respond,
                Code.LOGIN_LOCKED_DATES_UPDATE.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.SessionClientService.removeExpiredSession(..))"
            + "&&target(object)", returning = "respond", argNames = "respond,object")
    public void logExpiredSessionRemove(long respond, Object object) {
        StringBuilder message = new StringBuilder("Expired Sessions were removed in amount of: " + respond + " -- Class: " + object);
        logDataService.saveLog(new LogData(null, "Qty removed: " + respond,
                Code.SESSION_EXPIRED_REMOVE.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.UserClientService.accountChange(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void accountChange(UserRespondDto respond, Object object) {
        StringBuilder message = new StringBuilder("Attempt to change account Information for user with ID: " + respond.getId() + " with respond: "
                + respond.getRespond() + " -- Class: " + object);
        logDataService.saveLog(new LogData(respond.getId(), null, Code.USER_ACCOUNT_INFO_CHSNGE.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.UserClientService.changePassword(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond, object")
    public void logChangingPassword(UserRespondDto respond, Object object) {
        StringBuilder message = new StringBuilder("Attempt to change password for user with ID: " + respond.getId() + " with respond: "
                + respond.getRespond() + " -- Class: " + object);
        logDataService.saveLog(new LogData(respond.getId(), null, Code.USER_PASSWORD_CHANGED.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.UserClientService.resetPassword(..))"
            + "&&args(email) && target(object)" , returning = "respond", argNames = "respond, email, object")
    public void logChangingPassword(NotificationRespondDto respond, String email, Object object) {
        StringBuilder message = new StringBuilder("Attempt to reset password for: " + email + " with respond: "
                + respond.getMessage() + " -- Class: " + object);
        logDataService.saveLog(new LogData(null, null, Code.USER_PASSWORD_CHANGED.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.UserClientService.deleteUser(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logDeleteUser(UserRespondDto respond, Object object) {
        StringBuilder message = new StringBuilder("Attempt to delete user for user with ID: " + respond.getId() + " with respond: "
                + respond.getRespond() + " -- Class: " + object);
        logDataService.saveLog(new LogData(respond.getId(), null, Code.USER_DELETE.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.MatchClientService.uploadMatches(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logUploadMatches(MatchRespondDto respond, Object object) {
        StringBuilder message = new StringBuilder("Attempt to upload matches for League ID: " + respond.getLeagueId() + " with respond: "
                + respond.getRespond() + " -- Class: " + object);
        logDataService.saveLog(new LogData((long) respond.getLeagueId(), null, Code.UPLOAD_MATCHES_OK.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.MatchClientService.uploadMatchesFromLeagueConfigList(..))"
            + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logUploadAllMatches(MatchRespondDto respond, Object object) {
        StringBuilder message = new StringBuilder("Attempt to upload all matches with respond: "
                + respond.getRespond() + " -- Class: " + object);
        logDataService.saveLog(new LogData(null, "LeagueConfigList", Code.UPLOAD_ALL_MATCHES_OK.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.MatchPredictionClientService.savePredictions(..))"
            + "&&args(predictionDto) &&target(object)" ,returning = "respond", argNames = "predictionDto, respond, object")
    public void saveUserPredictions(PredictionDto predictionDto, String respond, Object object) {
        StringBuilder message = new StringBuilder("Attempt to save all matches predictions by user: " + predictionDto.getUserId() + " with respond: "
                + respond + " -- Class: " + object);
        logDataService.saveLog(new LogData(null, null, Code.SAVE_ALL_PREDICTIONS.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.MatchPredictionClientService.assignPoints(..))"
            + "&&target(object)" ,returning = "counter", argNames = "counter, object")
    public void assignPoints(long counter, Object object) {
        StringBuilder message = new StringBuilder("Attempt to assign points. Assign points to: " + counter + " predictions"
                + " -- Class: " + object);
        logDataService.saveLog(new LogData(null, null, Code.SAVE_ALL_PREDICTIONS.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.ClientService.RankingClientService." +
            "executeRankAssign(..))" + "&& target(object)" , argNames = "object")
    public void executeRankAssign(Object object) {
        StringBuilder message = new StringBuilder("Attempt to assign ranks." + " -- Class: " + object);
        logDataService.saveLog(new LogData(null, null, Code.ASSIGN_RANKS.getCode(), message.toString()));
        LOGGER.info(message.toString());
    }
}
