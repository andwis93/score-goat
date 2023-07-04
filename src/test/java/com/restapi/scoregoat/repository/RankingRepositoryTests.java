package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.Ranking;
import com.restapi.scoregoat.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RankingRepositoryTests {
    @Autowired
    private RankingRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByLeagueAndUserId() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Long userId = userRepository.save(user).getId();
        Ranking ranking = new Ranking(SeasonConfig.DEFAULT_LEAGUE.getId(), user);
        Long rankingId = repository.save(ranking).getId();

        //When
        Ranking theRanking = repository.findByLeagueIdAndUserId(SeasonConfig.DEFAULT_LEAGUE.getId(), userId);

        //Then
        assertEquals(userId, theRanking.getUser().getId());

        //CleanUp
        try{
            repository.deleteById(rankingId);
            userRepository.deleteById(userId);
        } catch (Exception ex) {
            // do nothing
        }
    }

    @Test
    void testExistsRankingByLeagueAndUserId() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Long userId = userRepository.save(user).getId();
        Ranking ranking = new Ranking(SeasonConfig.DEFAULT_LEAGUE.getId(), user);
        Long rankingId = repository.save(ranking).getId();

        //When
        boolean isExists = repository.existsRankingByLeagueIdAndUserId(SeasonConfig.DEFAULT_LEAGUE.getId(), userId);

        //Then
        assertTrue(isExists);

        //CleanUp
        try{
            repository.deleteById(rankingId);
            userRepository.deleteById(userId);
        } catch (Exception ex) {
            // do nothing
        }
    }
}
