package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        Optional<User> optionalUser = repository.findById(savedUser.getId());

        //Then
        assertTrue(optionalUser.isPresent());
        System.out.println(optionalUser);

        //CleanUp
        try{
            repository.deleteById(savedUser.getId());
        } catch (Exception ex) {
            //do Nothing
        }
    }

    @Test
    void testUserFindNameAndEmail() {
        //Given
        User user = new User("UserName", "UserEmail", "UserPassword");
        User savedUser =  repository.save(user);
        //When
        Optional<User> optionalUserName = repository.findByName("UserName");
        Optional<User> optionalUserEmail = repository.findByEmail("UserEmail");

        //Then
        assertTrue(optionalUserName.isPresent());
        assertTrue(optionalUserEmail.isPresent());
        System.out.println(optionalUserName);

        //CleanUp
        try{
            repository.deleteById(savedUser.getId());
        } catch (Exception ex) {
            //do nothing
        }
    }

    @Test
    void testDeleteUserById() {
        //Given
        User user = new User("UserName", "UserEmail", "UserPassword");
        Long originalSize = repository.count();
        User savedUser =  repository.save(user);
        Long sizeAfterSave = repository.count();

        //When
        repository.deleteById(savedUser.getId());
        Long sizeAfterDelete = repository.count();

        //Then
        assertEquals(originalSize, sizeAfterSave - 1);
        assertEquals(originalSize, sizeAfterDelete);
    }
}
