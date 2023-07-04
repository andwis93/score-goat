package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.domain.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTests {
    @Autowired
    private UserMapper mapper;

    @Test
    void tesMapUserDtoToUser() {
        //Given
        UserDto userDto = new UserDto("Name1", "Email1", "Password1");

        //When
        User user = mapper.mapUserDtoToUser(userDto);

        //Then
        assertEquals("Name1", user.getName());
        assertEquals("Email1", user.getEmail());
        assertEquals("Password1", user.getPassword());
    }
}
