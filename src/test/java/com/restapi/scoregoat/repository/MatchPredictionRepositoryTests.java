package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MatchPredictionRepositoryTests {
    @Autowired
    private MatchPredictionRepository repository;

    @Test
    void testExistsMatchPredictionByUserIdAndMatchId() {
        //Given
		User user = new User("Name1","Email1@test.com", "Password1");
		user.setId(12L);
		MatchPrediction prediction = new MatchPrediction();
		prediction.setPrediction(Prediction.HOME.getResult());
		prediction.setUser(user);
		prediction.setFixtureId(111L);
		Long matchPredictionId = repository.save(prediction).getId();

		//When
		boolean exist = repository.existsMatchPredictionByUserIdAndFixtureId(12L, 111L);

		//Then
		assertTrue(exist);

		//CleanUp
		try {
			repository.deleteById(matchPredictionId);
		} catch (Exception ex) {
			//do nothing
		}
    }
}
