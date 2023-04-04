package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.DurationManager;
import com.restapi.scoregoat.mapper.UserMapper;
import com.restapi.scoregoat.repository.LogInRepository;
import com.restapi.scoregoat.repository.UserRepository;
import com.restapi.scoregoat.validator.EmailValidator;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class UserService {
    private final UserRepository repository;
    private final LogInRepository logInRepository;
    private final LogInService logInService;
    private final SessionService sessionService;
    private final UserMapper mapper;
    private final EmailValidator validator;
    private final StrongPasswordEncryptor encryptor;
    private final DurationManager manager;

    public UserRespondDto signInUser(UserDto userDto) {
        if (userDto != null && userDto.getName() != null && userDto.getEmail() != null && userDto.getPassword() != null
                && userDto.getName().matches(".*\\w.*") && userDto.getEmail().matches(".*\\w.*")
                && userDto.getPassword().matches(".*\\w.*")) {
            User user = mapper.mapUserDtoToUser(userDto);
            if (validator.emailValidator(user.getEmail())) {
                if (repository.findByName(user.getName()).isEmpty()) {
                    if (repository.findByEmail(user.getEmail()).isEmpty()) {
                        user.setPassword(encryptor.encryptPassword(user.getPassword()));
                        user = repository.save(user);
                        return setRespond(user, Respond.USER_CREATED_OK.getRespond());
                    } else {
                        return new UserRespondDto(Respond.EMAIL_EXISTS.getRespond());
                    }
                } else {
                    return new UserRespondDto(Respond.USERNAME_EXISTS.getRespond());
                }
            } else {
                return new UserRespondDto(Respond.EMAIL_INCORRECT.getRespond());
            }
        } else {
            return new UserRespondDto(Respond.FIELDS_EMPTY.getRespond());
        }
    }

    public UserRespondDto changePassword(PasswordDto passwordDto) {
        if (passwordDto.getUserId() != null && passwordDto.getOldPassword() != null && passwordDto.getNewPassword() != null
                && passwordDto.getRepeatPassword()!= null && passwordDto.getOldPassword().matches(".*\\w.*")
                && passwordDto.getNewPassword().matches(".*\\w.*")
                && passwordDto.getRepeatPassword().matches(".*\\w.*")) {
            User user = repository.findById(passwordDto.getUserId()).orElse(null);
            if (user != null) {
                LogIn attempt = logInRepository.findByUser(user).orElse(new LogIn(user));
                if (attempt.getLocked() == null) {
                    if (encryptor.checkPassword(passwordDto.getOldPassword(), user.getPassword())) {
                        if (passwordDto.getNewPassword().equals(passwordDto.getRepeatPassword())) {
                            user.setPassword(encryptor.encryptPassword(passwordDto.getNewPassword()));
                            repository.save(user);
                            sessionService.saveRefreshedSession(user);
                            logInService.resetAttempt(attempt);
                            return new UserRespondDto().setExtendResponse(
                                    user, Respond.PASSWORD_CHANGED_OK.getRespond());

                        } else {
                            return new UserRespondDto(Respond.NEW_REPEAT_PASSWORD_DIFFERENT.getRespond());
                        }
                    } else {
                        attempt.addAttempt();
                        if (attempt.getAttempt() > DurationValues.MAX_ATTEMPT.getValue()) {
                            attempt.setLocked(LocalDateTime.now().plusHours(DurationValues.ATTEMPT_BLOCKED.getValue()));
                        }
                        logInRepository.save(attempt);
                        return new UserRespondDto(Respond.WRONG_OLD_PASSWORD.getRespond());
                    }
                } else {
                    if (manager.checkIfLessThen1H(attempt.getLocked())) {
                        return new UserRespondDto(Respond.TO_MANY_ATTEMPTS_LESS_THEN_1H.getRespond());
                    } else {
                        return new UserRespondDto(Respond.TO_MANY_ATTEMPTS.getRespond()
                                + manager.getDuration(attempt.getLocked()));
                    }
                }
            } else {
                return new UserRespondDto(Respond.USER_NOT_EXIST.getRespond());
            }
        } else {
            return new UserRespondDto(Respond.FIELDS_EMPTY.getRespond());
        }
    }

    public UserRespondDto accountChange(AccountDto accountDto){
        if (accountDto.getUserId() != null && accountDto.getPassword() != null &&
                accountDto.getPassword().matches(".*\\w.*")){
            User user = repository.findById(accountDto.getUserId()).orElse(null);
            if (user != null) {
                LogIn attempt = logInRepository.findByUser(user).orElse(new LogIn(user));
                if (attempt.getLocked() == null) {
                    if (encryptor.checkPassword(accountDto.getPassword(), user.getPassword())) {
                            user.setName(accountDto.getUserName());
                            user.setEmail(accountDto.getEmail());
                            repository.save(user);
                            sessionService.saveRefreshedSession(user);
                            logInService.resetAttempt(attempt);
                            return new UserRespondDto().setExtendResponse(
                                    user, Respond.ACCOUNT_CHANGED_OK.getRespond());
                    } else {
                        attempt.addAttempt();
                        if (attempt.getAttempt() > DurationValues.MAX_ATTEMPT.getValue()) {
                            attempt.setLocked(LocalDateTime.now().plusHours(DurationValues.ATTEMPT_BLOCKED.getValue()));
                        }
                        logInRepository.save(attempt);
                        return new UserRespondDto(Respond.WRONG_OLD_PASSWORD.getRespond());
                    }
                } else {
                    if (manager.checkIfLessThen1H(attempt.getLocked())) {
                        return new UserRespondDto(Respond.TO_MANY_ATTEMPTS_LESS_THEN_1H.getRespond());
                    } else {
                        return new UserRespondDto(Respond.TO_MANY_ATTEMPTS.getRespond()
                                + manager.getDuration(attempt.getLocked()));
                    }
                }
            } else {
                return new UserRespondDto(Respond.USER_NOT_EXIST.getRespond());
            }
        } else {
            return new UserRespondDto(Respond.FIELDS_EMPTY.getRespond());
        }
    }

    private UserRespondDto setRespond(User user, String respond) {
        UserRespondDto respondDto = new UserRespondDto(respond);
        respondDto.setId(user.getId());
        respondDto.setUserName(user.getName());
        respondDto.setEmail(user.getEmail());
        respondDto.setLogIn(true);
        return respondDto;
    }
}
