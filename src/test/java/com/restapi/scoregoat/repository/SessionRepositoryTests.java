package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Session;
import com.restapi.scoregoat.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SessionRepositoryTests {
    @Autowired
    private SessionRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindAll() {
        //Given
        Session session = new Session();

        //When
        Long id = repository.save(session).getId();
        List<Session> sessionList = repository.findAll().stream().filter(s -> s.getId().equals(id)).toList();

        //Then
        assertEquals(1, sessionList.size());

        //CleanUp
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            //do nothing
        }
    }


    @Test
    void testFindByUserId() {
        //Given
        User user = new User("UserName", "UserEmail", "UserPassword");
        Long userId  = userRepository.save(user).getId();
        Session session = new Session();
        session.setUser(user);

        //When
        Long id = repository.save(session).getId();
        Session theSession = Optional.of(repository.findByUserId(userId)).get().orElse(null);
        assert theSession != null;

        //Then
        assertEquals("UserName", theSession.getUser().getName());

        //CleanUp
        try {
            repository.deleteById(id);
            userRepository.deleteById(userId);
        } catch (Exception ex) {
            //do nothing
        }
    }
}
