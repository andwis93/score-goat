package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDBServiceTests {
    @Autowired
    private UserDBService service;
    @Autowired
    private UserRepository repository;

    @Test
    void testNameExistsCheck() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Long userID = service.save(user).getId();

        //When
        boolean nameExists1 = service.existsByName("Name1");
        boolean nameExists2 = service.existsByName("Name2");

        //Then
        assertTrue(nameExists1);
        assertFalse(nameExists2);

        //CleanUp
        try {
            repository.deleteById(userID);
        } catch (Exception ex) {
            //do nothing
        }
    }

    @Test
    void testEmailExistsCheck() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Long userID = service.save(user).getId();

        //When
        boolean emailExists1 = service.existsByEmail("Email1@test.com");
        boolean emailExists2 = service.existsByEmail("Email2@test.com");

        //Then
        assertTrue(emailExists1);
        assertFalse(emailExists2);

        //CleanUp
        try {
            repository.deleteById(userID);
        } catch (Exception ex) {
            //do nothing
        }
    }
}
