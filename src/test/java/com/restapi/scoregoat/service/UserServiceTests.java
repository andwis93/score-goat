package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.mapper.UserMapper;
import com.restapi.scoregoat.repository.UserRepository;
import com.restapi.scoregoat.validator.EmailValidator;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private EmailValidator validator;
    @Mock
    private StrongPasswordEncryptor encryptor;

    @Test
    void testCreateUser() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        UserDto userDto = new UserDto("NameDto1", "EmailDto1@test.com", "PasswordDto1");

        when(mapper.mapUserDtoToUser(userDto)).thenReturn(user);
        when(validator.emailValidator(user.getEmail())).thenReturn(true);
        when(repository.findByName(user.getName())).thenReturn(Optional.empty());
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(encryptor.encryptPassword(user.getPassword())).thenReturn("Encrypted password");
        when(repository.save(user)).thenReturn(user);

        //When
        UserRespondDto respondDto = service.signInUser(userDto);

        //Then
        assertEquals(Respond.USER_CREATED_OK.getRespond(), respondDto.getRespond());
        assertEquals(WindowStatus.CLOSE.getStatus(), respondDto.getWindowStatus());
    }
}
