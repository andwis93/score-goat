package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.domain.Ranking;
import com.restapi.scoregoat.domain.RankingNorm;
import com.restapi.scoregoat.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RankingManagerTests {

    @Autowired
    private RankManager rankManager;

    @Test
    void testSetRankingNorms() {
        //Given
        User user1 = new User("Name1","Email1@test.com", "Password1");
        user1.setId(1L);
        Ranking ranking1 = new Ranking(200L, -2, 3, user1,39,1,0,3);

        User user2 = new User("Name2","Email2@test.com", "Password2");
        user2.setId(1L);
        Ranking ranking2 = new Ranking(100L, 14, 1, user2,39, 2,8,1);

        User user3 = new User("Name3","Email3@test.com", "Password3");
        user3.setId(1L);
        Ranking ranking3 = new Ranking(300L, -2, 3, user3,39, 4,17,3);

        User user4 = new User("Name4","Email4@test.com", "Password4");
        user4.setId(1L);
        Ranking ranking4 = new Ranking(400L, 8, 2, user4,39, 3,24,2);

        List<Ranking> rankings = new ArrayList<>();
        rankings.add(ranking1);
        rankings.add(ranking2);
        rankings.add(ranking3);
        rankings.add(ranking4);

        //When
        RankingNorm norm = rankManager.setRankingNorms(rankings);

        //Then
        assertEquals(2, norm.getMiddle());
        assertEquals(3, norm.getLast());
    }
}
