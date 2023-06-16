package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.Graduation;
import com.restapi.scoregoat.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class GraduationRepositoryTests {
    @Autowired
    private GraduationRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByLeagueAndUserId() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Long userId = userRepository.save(user).getId();
        Graduation graduation = new Graduation(SeasonConfig.DEFAULT_LEAGUE.getId(), user);
        Long graduationId = repository.save(graduation).getId();

        //When
        Graduation theGraduation = repository.findByLeagueAndUserId(SeasonConfig.DEFAULT_LEAGUE.getId(), userId);

        //Then
        assertEquals(userId, theGraduation.getUser().getId());

        //CleanUp
        try{
            repository.deleteById(graduationId);
            userRepository.deleteById(userId);
        } catch (Exception ex) {
            // do nothing
        }
    }

    @Test
    void testExistsGraduationByLeagueAndUserId() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Long userId = userRepository.save(user).getId();
        Graduation graduation = new Graduation(SeasonConfig.DEFAULT_LEAGUE.getId(), user);
        Long graduationId = repository.save(graduation).getId();

        //When
        boolean isExists = repository.existsGraduationByLeagueAndUserId(SeasonConfig.DEFAULT_LEAGUE.getId(), userId);

        //Then
        assertTrue(isExists);

        //CleanUp
        try{
            repository.deleteById(graduationId);
            userRepository.deleteById(userId);
        } catch (Exception ex) {
            // do nothing
        }
    }
}
