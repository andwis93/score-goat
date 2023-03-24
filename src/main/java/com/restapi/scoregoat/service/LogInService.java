package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.repository.LogInRepository;
import com.restapi.scoregoat.repository.SessionRepository;
import com.restapi.scoregoat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class LogInService {
    private static final int MAX_ATTEMPT = 5;
    private LogInRepository repository;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private StrongPasswordEncryptor encryptor;

    public UserRespondDto logInAttempt(UserParam userParam) {
        User user = checkIfUserExist(userParam.getName());
        if (user != null) {
            LogIn attempt = repository.findByUser(user).orElse(new LogIn(user));
            if (attempt.getLocked() == null) {
                if (encryptor.checkPassword(userParam.getPassword(), user.getPassword())) {

                    Session session = sessionRepository.findByUserId(user.getId()).orElse(new Session());
                    session.setUser(user);
                    refreshSession(session);
                    sessionRepository.save(session);

                    attempt.resetAttempt();
                    repository.save(attempt);

                    UserRespondDto respondDto = new UserRespondDto(Respond.USER_LOGGED_IN.getRespond(), WindowStatus.CLOSE.getStatus());
                    respondDto.setId(user.getId());
                    respondDto.setUserName(user.getName());
                    respondDto.setEmail(user.getEmail());
                    respondDto.setLogIn(true);
                    return respondDto;
                } else {
                    attempt.addAttempt();
                    if (attempt.getAttempt() > MAX_ATTEMPT) {
                        attempt.setLocked(LocalDateTime.now().plusHours(24));
                    }
                    repository.save(attempt);
                    return new UserRespondDto(Respond.WRONG_PASSWORD.getRespond(), WindowStatus.OPEN.getStatus());
                }
            } else {

                return new UserRespondDto(Respond.TO_MANY_ATTEMPTS.getRespond() + getDuration(attempt.getLocked()), WindowStatus.OPEN.getStatus());
            }
        } else {
            return new UserRespondDto(Respond.USER_NOT_EXIST.getRespond(), WindowStatus.OPEN.getStatus());
        }
    }

    public void refreshSession(Session session) {
        session.setEnd(LocalDateTime.now());
    }

    private User checkIfUserExist(String name) {
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

    private String getDuration(LocalDateTime time) {
        Duration diff = Duration.between(time, LocalDateTime.now());
        String timeRemain = String.format("%d:%02d:%02d",
                diff.toHours(),
                diff.toMinutesPart(),
                diff.toSecondsPart());
        return timeRemain.replace("-","");
    }
}
