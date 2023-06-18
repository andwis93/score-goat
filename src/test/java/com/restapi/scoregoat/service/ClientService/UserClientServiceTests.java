package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.mapper.UserMapper;
import com.restapi.scoregoat.service.DBService.LogInDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import com.restapi.scoregoat.validator.EmailValidator;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserClientServiceTests {
    @InjectMocks
    private UserClientService service;
    @Mock
    private UserDBService dbService;
    @Mock
    private LogInDBService logInService;
    @Mock
    private SessionClientService sessionService;
    @Mock
    private UserMapper mapper;
    @Mock
    private EmailValidator validator;
    @Mock
    private StrongPasswordEncryptor encryptor;

    @Test
    void testSignUpUser() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        UserDto userDto = new UserDto("NameDto1", "EmailDto1@test.com", "PasswordDto1");

        when(mapper.mapUserDtoToUser(userDto)).thenReturn(user);
        when(validator.emailValidator(user.getEmail())).thenReturn(true);
        when(dbService.existsByName(user.getName())).thenReturn(false);
        when(dbService.existsByEmail(user.getEmail())).thenReturn(false);
        when(encryptor.encryptPassword(user.getPassword())).thenReturn("Encrypted password");
        when(dbService.save(user)).thenReturn(user);

        //When
        UserRespondDto respondDto = service.signUpUser(userDto);

        //Then
        assertEquals(Respond.USER_CREATED_OK.getRespond(), respondDto.getRespond());
        verify(dbService, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(1L);
        UserDto userDto = new UserDto("Name1", "Email1@test.com", "Password1");
        userDto.setId(1L);

        when(dbService.findById(userDto.getId())).thenReturn(user);
        when(dbService.deleteById(user.getId())).thenReturn(true);
        when(encryptor.checkPassword("Password1", "Password1")).thenReturn(true);

        //When
        UserRespondDto respond = service.deleteUser(userDto);

        //Then
        assertEquals("Name1", respond.getUserName());
        assertEquals("Email1@test.com", respond.getEmail());
        assertEquals(Respond.USER_DELETED_OK.getRespond(), respond.getRespond());
        verify(dbService, times(1)).deleteById(user.getId());
    }

    @Test
    void testChangePassword() {
        //Given
        User user = new User("Name1","Email1@test.com", "OldPassword");
        LogIn attempt = new LogIn(user);
        PasswordDto passwordDto = new PasswordDto(1L, "OldPassword", "MatchPassword", "MatchPassword");
        when(dbService.findById(1L)).thenReturn(user);
        when(logInService.findByUser(user)).thenReturn(attempt);
        when(encryptor.checkPassword(passwordDto.getOldPassword(), user.getPassword())).thenReturn(true);
        when(sessionService.saveRefreshedSession(any())).thenReturn(true);
        when(logInService.resetAttempt(attempt)).thenReturn(true);

        //When
        UserRespondDto respondDto = service.changePassword(passwordDto);

        //Then
        assertEquals("Name1", respondDto.getUserName());
        assertEquals("Email1@test.com", respondDto.getEmail());
        assertEquals(Respond.PASSWORD_CHANGED_OK.getRespond(), respondDto.getRespond());
        verify(dbService, times(1)).save(user);
    }

    @Test
    void testChangeAccountInformation() {
        //Given
        User user = new User("Name1","Email1@test.com", "password");
        user.setId(1L);
        LogIn attempt = new LogIn(user);
        AccountDto accountDto = new AccountDto(1L, "Name1", "Email1@test.com", "password");
        when(dbService.findById(1L)).thenReturn(user);
        when(dbService.nameExistCheck(accountDto.getUserId(), accountDto.getUserName())).thenReturn(true);
        when(dbService.emailExistCheck(accountDto.getUserId(), accountDto.getEmail())).thenReturn(true);
        when(logInService.findByUser(user)).thenReturn(attempt);
        when(encryptor.checkPassword(accountDto.getPassword(), user.getPassword())).thenReturn(true);
        when(sessionService.saveRefreshedSession(any())).thenReturn(true);
        when(logInService.resetAttempt(attempt)).thenReturn(true);

        //When
        UserRespondDto respondDto = service.accountChange(accountDto);

        //Then
        assertEquals("Name1", respondDto.getUserName());
        assertEquals("Email1@test.com", respondDto.getEmail());
        assertEquals(Respond.ACCOUNT_CHANGED_OK.getRespond(), respondDto.getRespond());
        verify(dbService, times(1)).save(user);
    }
}
