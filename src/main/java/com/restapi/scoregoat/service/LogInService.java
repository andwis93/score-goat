package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.DurationManager;
import com.restapi.scoregoat.repository.LogInRepository;
import com.restapi.scoregoat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class LogInService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogInService.class);
    private LogInRepository repository;
    private UserRepository userRepository;
    private StrongPasswordEncryptor encryptor;
    private SessionService sessionService;
    private LogDataService logDataService;
    private DurationManager manager;

    public UserRespondDto logInAttempt(final UserDto userDto) {
        User user = checkIfUserExist(userDto.getName());
        if (user != null) {
            LogIn attempt = repository.findByUser(user).orElse(new LogIn(user));
            if (attempt.getLocked() == null) {
                if (encryptor.checkPassword(userDto.getPassword(), user.getPassword())) {
                    sessionService.saveRefreshedSession(user);
                    resetAttempt(attempt);
                    return new UserRespondDto().setExtendResponse(
                            user, Respond.USER_LOGGED_IN.getRespond(), NotificationType.SUCCESS.getType());
                } else {
                    attempt.addAttempt();
                    if (attempt.getAttempt() > DurationValues.MAX_ATTEMPT.getValue()) {
                        attempt.setLocked(LocalDateTime.now().plusHours(DurationValues.ATTEMPT_BLOCKED.getValue()));
                    }
                    repository.save(attempt);
                    return new UserRespondDto(Respond.WRONG_PASSWORD.getRespond(), NotificationType.ERROR.getType());
                }
            } else {
                if (manager.checkIfLessThen1H(attempt.getLocked())) {
                    return new UserRespondDto(Respond.TO_MANY_ATTEMPTS_LESS_THEN_1H.getRespond(), NotificationType.ERROR.getType());
                } else {
                    return new UserRespondDto(Respond.TO_MANY_ATTEMPTS.getRespond()
                            + manager.getDuration(attempt.getLocked()), NotificationType.ERROR.getType());
                }
            }
        } else {
            return new UserRespondDto(Respond.USER_NOT_EXIST.getRespond(), NotificationType.ERROR.getType());
        }
    }
    private User checkIfUserExist(final String name) {
        if (userRepository.findByName(name).isPresent()) {
            return userRepository.findByName(name).orElse(null);
        } else {
            if (userRepository.findByEmail(name).isPresent()) {
                return userRepository.findByEmail(name).orElse(null);
            } else {
                return null;
            }
        }
    }
    public boolean resetAttempt(final LogIn attempt) {
        try {
            attempt.resetAttempt();
            repository.save(attempt);
            return true;
        } catch (IllegalArgumentException ex) {
            String message = ex.getMessage() + "  --ERROR: Couldn't execute \"resetAttempt\"-- ";
            logDataService.saveLog(new LogData(
                    null, "With UserID: " + attempt.getUser(), Code.LOGIN_RESET_ATTEMPT_ERROR.getCode(), message));
            LOGGER.error(message, ex);
            return false;
        }
    }
}
