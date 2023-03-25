package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.mapper.UserMapper;
import com.restapi.scoregoat.repository.UserRepository;
import com.restapi.scoregoat.validator.EmailValidator;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final EmailValidator validator;
    private final StrongPasswordEncryptor encryptor;

    public UserRespondDto signInUser(UserDto userDto) {
        if (userDto != null && userDto.getName() != null && userDto.getEmail() != null && userDto.getPassword() != null
                && userDto.getName().matches(".*\\w.*") && userDto.getEmail().matches(".*\\w.*")
                && userDto.getPassword().matches(".*\\w.*")) {
            User user = mapper.mapUserDtoToUser(userDto);
            if (validator.emailValidator(user.getEmail())) {
                if (repository.findByName(user.getName()).isEmpty()) {
                    if (repository.findByEmail(user.getEmail()).isEmpty()) {
                        user.setPassword(encryptor.encryptPassword(user.getPassword()));
                        Long id = repository.save(user).getId();
                        return setRespond(id, user);
                    } else {
                        return new UserRespondDto(Respond.EMAIL_EXISTS.getRespond(), WindowStatus.OPEN.getStatus());
                    }
                } else {
                    return new UserRespondDto(Respond.USERNAME_EXISTS.getRespond(), WindowStatus.OPEN.getStatus());
                }
            } else {
                return new UserRespondDto(Respond.EMAIL_INCORRECT.getRespond(), WindowStatus.OPEN.getStatus());
            }
        } else {
            return new UserRespondDto(Respond.USER_EMPTY.getRespond(), WindowStatus.OPEN.getStatus());
        }
    }

    private UserRespondDto setRespond(Long id, User user) {
        UserRespondDto respondDto = new UserRespondDto( Respond.USER_CREATED_OK.getRespond(), WindowStatus.CLOSE.getStatus());
        respondDto.setId(id);
        respondDto.setUserName(user.getName());
        respondDto.setEmail(user.getEmail());
        respondDto.setLogIn(true);
        return respondDto;
    }
}
