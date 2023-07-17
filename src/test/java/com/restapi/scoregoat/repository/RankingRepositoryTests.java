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
    void testFindByUserIdAndLeagueId() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Long userId = userRepository.save(user).getId();
        Ranking ranking = new Ranking(user, SeasonConfig.DEFAULT_LEAGUE.getId());
        Long rankingId = repository.save(ranking).getId();

        //When
        Ranking theRanking = repository.findByUserIdAndLeagueId(userId, SeasonConfig.DEFAULT_LEAGUE.getId());

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
    void testExistsRankingByUserIdAndLeagueId() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Long userId = userRepository.save(user).getId();
        Ranking ranking = new Ranking(user, SeasonConfig.DEFAULT_LEAGUE.getId());
        Long rankingId = repository.save(ranking).getId();

        //When
        boolean isExists = repository.existsRankingByUserIdAndLeagueId(userId, SeasonConfig.DEFAULT_LEAGUE.getId());

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
