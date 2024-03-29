package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.repository.LogInRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class LogInDBService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogInDBService.class);
    private final LogInRepository repository;
    private final LogDataDBService logDataService;

    public List<LogIn> findAll() {
        return repository.findAll();
    }
    public LogIn findByUser(User user) {
        return repository.findByUser(user).orElse(new LogIn(user));
    }
    public void save(LogIn attempt) {
        repository.save(attempt);
    }
    public void saveAll(List<LogIn> logInList) {
        repository.saveAll(logInList);
    }

    public boolean resetAttempt(final LogIn attempt) {
        try {
            attempt.resetAttempt();
            repository.save(attempt);
            return true;
        } catch (IllegalArgumentException ex) {
            StringBuilder message = new StringBuilder(ex.getMessage() + "  --ERROR: Couldn't execute \"resetAttempt\"-- ");
            logDataService.saveLog(new LogData(
                    null, "With UserID: " + attempt.getUser(),
                    Code.LOGIN_RESET_ATTEMPT_ERROR.getCode(), message.toString()));
            LOGGER.error(message.toString(), ex);
            return false;
        }
    }
    public void delete(LogIn logIn) {
        repository.delete(logIn);
    }

    public Set<LogIn> findBeforeDate(LocalDate date) {
        return repository.findByLastLoggedInBefore(date);
    }
}
