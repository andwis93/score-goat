package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.domain.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public User mapUserDtoToUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail(),
                userDto.getPassword()
        );
    }
}
