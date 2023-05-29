package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.DurationManager;
import com.restapi.scoregoat.mapper.UserMapper;
import com.restapi.scoregoat.repository.LogInRepository;
import com.restapi.scoregoat.repository.UserRepository;
import com.restapi.scoregoat.validator.EmailValidator;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

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

    public UserRespondDto signUpUser(UserDto userDto) {
        if (userDto != null && userDto.getName() != null && userDto.getEmail() != null && userDto.getPassword() != null
                && userDto.getName().matches(".*\\w.*") && userDto.getEmail().matches(".*\\w.*")
                && userDto.getPassword().matches(".*\\w.*")) {
            User user = mapper.mapUserDtoToUser(userDto);
            if (validator.emailValidator(user.getEmail())) {
                if (repository.findByName(user.getName()).isEmpty()) {
                    if (repository.findByEmail(user.getEmail()).isEmpty()) {
                        user.setPassword(encryptor.encryptPassword(user.getPassword()));
                        user = repository.save(user);
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
                                    user, Respond.PASSWORD_CHANGED_OK.getRespond(), NotificationType.SUCCESS.getType());

                        } else {
                            return new UserRespondDto(Respond.NEW_REPEAT_PASSWORD_DIFFERENT.getRespond(), NotificationType.ERROR.getType());
                        }
                    } else {
                        attempt.addAttempt();
                        if (attempt.getAttempt() > DurationValues.MAX_ATTEMPT.getValue()) {
                            attempt.setLocked(LocalDateTime.now().plusHours(DurationValues.ATTEMPT_BLOCKED.getValue()));
                        }
                        logInRepository.save(attempt);
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
            User user = repository.findById(accountDto.getUserId()).orElse(null);
            if (user != null) {
                if (emailExistCheck(accountDto.getUserId(), accountDto.getEmail())) {
                    LogIn attempt = logInRepository.findByUser(user).orElse(new LogIn(user));
                    if (attempt.getLocked() == null) {
                        if (encryptor.checkPassword(accountDto.getPassword(), user.getPassword())) {
                            user.setName(accountDto.getUserName());
                            user.setEmail(accountDto.getEmail());
                            repository.save(user);
                            sessionService.saveRefreshedSession(user);
                            logInService.resetAttempt(attempt);
                            return new UserRespondDto().setExtendResponse(
                                    user, Respond.ACCOUNT_CHANGED_OK.getRespond(), NotificationType.SUCCESS.getType());
                        } else {
                            attempt.addAttempt();
                            if (attempt.getAttempt() > DurationValues.MAX_ATTEMPT.getValue()) {
                                attempt.setLocked(LocalDateTime.now().plusHours(DurationValues.ATTEMPT_BLOCKED.getValue()));
                            }
                            logInRepository.save(attempt);
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
                return new UserRespondDto(Respond.USER_NOT_EXIST.getRespond(), NotificationType.ERROR.getType());
            }
        } else {
            return new UserRespondDto(Respond.FIELDS_EMPTY.getRespond(), NotificationType.ERROR.getType());
        }
    }
    private boolean emailExistCheck(Long userId, String email) {
        List<User> usersWithEmail = repository.findAllByEmail(email).stream().filter(
                user -> !user.getId().equals(userId)).toList();
        return usersWithEmail.size() == 0;
    }

    public void deleteUser(Long userId) {
      repository.deleteById(userId);
    }
}
