package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.DurationManager;
import com.restapi.scoregoat.mapper.UserMapper;
import com.restapi.scoregoat.service.DBService.LogInDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import com.restapi.scoregoat.validator.EmailValidator;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class UserClientService {
    private final UserDBService userService;
    private final LogInDBService logInDBService;
    private final SessionClientService sessionService;
    private final UserMapper mapper;
    private final EmailValidator validator;
    private final StrongPasswordEncryptor encryptor;
    private final DurationManager manager;

    public UserRespondDto signUpUser(UserDto userDto) {
        if (userDto != null && userDto.getName() != null && userDto.getEmail() != null && userDto.getPassword() != null
                && userDto.getName().matches(".*\\w.*") && userDto.getEmail().matches(".*\\w.*")
                && userDto.getPassword().matches(".*\\w.*")) {
            User user = mapper.mapUserDtoToUser(userDto);
            if (validator.emailValidator(user.getEmail())) {
                if (!userService.existsByName(user.getName())) {
                    if (!userService.existsByEmail(user.getEmail())) {
                        user.setPassword(encryptor.encryptPassword(user.getPassword()));
                        user = userService.save(user);
                        return new UserRespondDto().setExtendResponse(user, Respond.USER_CREATED_OK.getRespond(),
                                NotificationType.SUCCESS.getType());
                    } else {
                        return new UserRespondDto(Respond.EMAIL_EXISTS.getRespond(), NotificationType.ERROR.getType());
                    }
                } else {
                    return new UserRespondDto(Respond.USERNAME_EXISTS.getRespond(), NotificationType.ERROR.getType());
                }
            } else {
                return new UserRespondDto(Respond.EMAIL_INCORRECT.getRespond(), NotificationType.ERROR.getType());
            }
        } else {
            return new UserRespondDto(Respond.FIELDS_EMPTY.getRespond(), NotificationType.ERROR.getType());
        }
    }

    public UserRespondDto changePassword(@NotNull PasswordDto passwordDto) {
        if (passwordDto.getUserId() != null && passwordDto.getOldPassword() != null && passwordDto.getNewPassword() != null
                && passwordDto.getRepeatPassword()!= null && passwordDto.getOldPassword().matches(".*\\w.*")
                && passwordDto.getNewPassword().matches(".*\\w.*")
                && passwordDto.getRepeatPassword().matches(".*\\w.*")) {
            User user = userService.findById(passwordDto.getUserId());
            if (user != null) {
                LogIn attempt = logInDBService.findByUser(user);
                if (attempt.getLocked() == null) {
                    if (encryptor.checkPassword(passwordDto.getOldPassword(), user.getPassword())) {
                        if (passwordDto.getNewPassword().equals(passwordDto.getRepeatPassword())) {
                            user.setPassword(encryptor.encryptPassword(passwordDto.getNewPassword()));
                            userService.save(user);
                            sessionService.saveRefreshedSession(user);
                            logInDBService.resetAttempt(attempt);
                            return new UserRespondDto().setExtendResponse(
                                    user, Respond.PASSWORD_CHANGED_OK.getRespond(), NotificationType.SUCCESS.getType());
                        } else {
                            return new UserRespondDto(Respond.NEW_REPEAT_PASSWORD_DIFFERENT.getRespond(), NotificationType.ERROR.getType());
                        }
                    } else {
                        attempt.addAttempt();
                        if (attempt.getAttempt() > DurationValues.MAX_ATTEMPT.getValue()) {
                            attempt.setLocked(LocalDateTime.now().plusHours(DurationValues.ATTEMPT_BLOCKED.getValue()));
                        }
                        logInDBService.save(attempt);
                        return new UserRespondDto(Respond.WRONG_OLD_PASSWORD.getRespond(), NotificationType.ERROR.getType());
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
        } else {
            return new UserRespondDto(Respond.FIELDS_EMPTY.getRespond(), NotificationType.ERROR.getType());
        }
    }

    public UserRespondDto accountChange(@NotNull AccountDto accountDto){
        if (accountDto.getUserId() != null && accountDto.getPassword() != null &&
                accountDto.getPassword().matches(".*\\w.*")){
            User user = userService.findById(accountDto.getUserId());
            if (user != null) {
                if (userService.nameExistCheck(accountDto.getUserId(), accountDto.getUserName())) {
                    if (userService.emailExistCheck(accountDto.getUserId(), accountDto.getEmail())) {
                        LogIn attempt = logInDBService.findByUser(user);
                        if (attempt.getLocked() == null) {
                            if (encryptor.checkPassword(accountDto.getPassword(), user.getPassword())) {
                                user.setName(accountDto.getUserName());
                                user.setEmail(accountDto.getEmail());
                                userService.save(user);
                                sessionService.saveRefreshedSession(user);
                                logInDBService.resetAttempt(attempt);
                                return new UserRespondDto().setExtendResponse(
                                        user, Respond.ACCOUNT_CHANGED_OK.getRespond(), NotificationType.SUCCESS.getType());
                            } else {
                                attempt.addAttempt();
                                if (attempt.getAttempt() > DurationValues.MAX_ATTEMPT.getValue()) {
                                    attempt.setLocked(LocalDateTime.now().plusHours(DurationValues.ATTEMPT_BLOCKED.getValue()));
                                }
                                logInDBService.save(attempt);
                                return new UserRespondDto(Respond.WRONG_OLD_PASSWORD.getRespond(), NotificationType.ERROR.getType());
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
                        return new UserRespondDto(Respond.EMAIL_EXISTS.getRespond(), NotificationType.ERROR.getType());
                    }
                } else {
                    return new UserRespondDto(Respond.USERNAME_EXISTS.getRespond(), NotificationType.ERROR.getType());
                }
            } else {
                return new UserRespondDto(Respond.USER_NOT_EXIST.getRespond(), NotificationType.ERROR.getType());
            }
        } else {
            return new UserRespondDto(Respond.FIELDS_EMPTY.getRespond(), NotificationType.ERROR.getType());
        }
    }

    public UserRespondDto deleteUser(UserDto userDto) {
        if (userDto.getId() != null && userDto.getPassword() != null ) {
            User user = userService.findById(userDto.getId());
            if (user != null) {
                if (encryptor.checkPassword(userDto.getPassword(), user.getPassword())) {
                    logInDBService.delete(user.getLogIn());
                    userService.deleteById(user.getId());
                    return new UserRespondDto().setExtendResponse(
                            user, Respond.USER_DELETED_OK.getRespond(), NotificationType.SUCCESS.getType());
                } else {
                    return new UserRespondDto(Respond.WRONG_PASSWORD.getRespond(), NotificationType.ERROR.getType());
                }
            } else {
                return new UserRespondDto(Respond.USER_NOT_EXIST.getRespond(), NotificationType.ERROR.getType());
            }
        } else {
            return new UserRespondDto(Respond.FIELDS_EMPTY.getRespond(), NotificationType.ERROR.getType());
        }
    }
}
