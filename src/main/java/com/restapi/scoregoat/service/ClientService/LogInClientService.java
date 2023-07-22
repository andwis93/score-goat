package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.DurationManager;
import com.restapi.scoregoat.repository.LogInRepository;
import com.restapi.scoregoat.service.DBService.LogInDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class LogInClientService {
    private final LogInRepository repository;
    private final LogInDBService dbService;
    private final UserDBService userDBService;
    private final SessionClientService sessionService;
    private final StrongPasswordEncryptor encryptor;
    private final DurationManager manager;

    public UserRespondDto logInAttempt(UserDto userDto) {
        User user = checkIfUserExist(userDto.getName());
        if (user != null) {
            LogIn attempt = dbService.findByUser(user);
            if (attempt.getLocked() == null) {
                if (encryptor.checkPassword(userDto.getPassword(), user.getPassword())) {
                    sessionService.createSession(user);
                    dbService.resetAttempt(attempt);
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

    public long updateLogInLockedDates(){
        List<LogIn> expiredLocks = dbService.findAll().stream()
                .filter(empty -> empty.getLocked() != null)
                .filter(time -> time.getLocked().isBefore(LocalDateTime.now()))
                .toList();
        expiredLocks.forEach(LogIn::removeLocked);
        dbService.saveAll(expiredLocks);
        return expiredLocks.size();
    }

    private User checkIfUserExist(final String identification) {
        if (userDBService.existsByName(identification)) {
            return userDBService.findByName(identification);
        } else {
            if (userDBService.existsByEmail(identification)) {
                return userDBService.findByEmail(identification);
            } else {
                return null;
            }
        }
    }

    public void deleteUnActiveUsers(LocalDate date) {
      Set<LogIn> logInWithUnActiveUsersList = dbService.findBeforeDate(date);
      repository.deleteAll(logInWithUnActiveUsersList);
    }

}
