package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.LogIn;
import com.restapi.scoregoat.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LogInRepositoryTests {
    @Autowired
    private LogInRepository repository;

    @Test
    void testFindAndSaveAll() {
        //Given
        User user = new User("UserNameTest", "UserEmailTest", "UserPasswordTest");
        LogIn logIn = new LogIn(user);
        List<LogIn> logInList = new ArrayList<>();
        logInList.add(logIn);

        //When

        repository.saveAll(logInList);
        List<LogIn> logInListFound = repository.findAll().stream()
                .filter(userList -> userList.getUser() != null)
                .filter(list -> list.getUser().getName().equals("UserNameTest")).toList();

        //Then
        assertEquals(1, logInListFound.size());

        //CleanUp
        try{
            for(LogIn log: logInListFound) {
                repository.deleteById(log.getId());
            }
        } catch (Exception ex) {
            //do nothing
        }
    }

    @Test
    void testFindByUser() {
        //Given
        User user = new User("UserNameTest", "UserEmailTest", "UserPasswordTest");
        LogIn logIn = new LogIn(user);
        List<LogIn> logInList = new ArrayList<>();
        logInList.add(logIn);
        repository.saveAll(logInList);

        //When

        LogIn logInFoundByUser = Optional.of(repository.findByUser(user)).get().orElse(null);
        assert logInFoundByUser != null;

        //Then
        assertEquals("UserNameTest", logInFoundByUser.getUser().getName());

        //CleanUp
        try{
            repository.deleteById(logInFoundByUser.getId());
        } catch (Exception ex) {
            //Do nothing
        }
    }
}

