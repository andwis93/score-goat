package com.restapi.scoregoat.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class PasswordManagerTests {
    @Autowired
    private PasswordManager passwordManager;

    @Test
    public void shouldGeneratePassword() {
        //Given
        //When
        String password = passwordManager.generateRandomString(10);

        //Then
        assertEquals(10, password.length());
    }
}
