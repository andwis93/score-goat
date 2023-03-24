package com.restapi.scoregoat.watcher;

import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.domain.UserRespondDto;
import com.restapi.scoregoat.service.LogDataService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Aspect
@Component
public class Watcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Watcher.class);
    private final LogDataService logDataDbService;

    @Before("execution(* com.restapi.scoregoat.service.SeasonService.setSeason(..))"
            + "&& args(id) && target(object)")
    public void logSetSeason(int id, Object object) {
        String message = "Set Season -- Class: " + object.getClass().getName();
        logDataDbService.saveLog(new LogData(null,"League ID: " + id, message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.UserService.signInUser(..))" + "&&target(object)" , returning = "respond", argNames = "respond,object")
    public void logSetCreateUser(UserRespondDto respond, Object object) {
        String message = "Create User with respond: " + respond.getRespond() + " -- Class: " + object;
        logDataDbService.saveLog(new LogData(respond.getId(), null, message));
        LOGGER.info(message);
    }

    @AfterReturning(value = "execution(* com.restapi.scoregoat.service.LogInService.logInAttempt(..))" + "&&target(object)", returning = "respond", argNames = "respond,object")
    public void logLogIn(UserRespondDto respond, Object object) {
        String message = "Attempt to Log In with respond: " + respond.getRespond() + " -- Class: " + object;
        logDataDbService.saveLog(new LogData(respond.getId(), null, message));
        LOGGER.info(message);
    }
}
