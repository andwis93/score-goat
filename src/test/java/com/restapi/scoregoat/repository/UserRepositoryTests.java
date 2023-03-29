package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository repository;

    @Test
    void testUserSave() {
        //Given
        User user = new User("UserName", "UserEmail", "UserPassword");

        //When
        User savedUser =  repository.save(user);
        Long id = savedUser.getId();

        Optional<User> optionalUser = repository.findById(id);

        //Then
        assertTrue(optionalUser.isPresent());
        System.out.println(optionalUser);

        //CleanUp
        try{
            repository.deleteById(id);
        } catch (Exception ex) {
            //do Nothing
        }
    }

    @Test
    void testUserFindNameAndEmail() {
        //Given
        User user = new User("UserName", "UserEmail", "UserPassword");
        User savedUser =  repository.save(user);
        Long id = savedUser.getId();
        //When
        Optional<User> optionalUserName = repository.findByName("UserName");
        Optional<User> optionalUserEmail = repository.findByEmail("UserEmail");

        //Then
        assertTrue(optionalUserName.isPresent());
        assertTrue(optionalUserEmail.isPresent());
        System.out.println(optionalUserName);

        //CleanUp
        try{
            repository.deleteById(id);
        } catch (Exception ex) {
            //do nothing
        }
    }
}
