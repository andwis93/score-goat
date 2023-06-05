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
	@Autowired
	private UserRepository userRepository;

    @Test
    void testExistsMatchPredictionByUserIdAndMatchId() {
        //Given
		User user = new User("Name1","Email1@test.com", "Password1");
		Long userId = userRepository.save(user).getId();
		MatchPrediction prediction = new MatchPrediction();
		prediction.setPrediction(Result.HOME.getResult());
		prediction.setResult(Result.UNSET.getResult());
		prediction.setUser(user);
		prediction.setFixtureId(17711L);
		Long matchPredictionId = repository.save(prediction).getId();

		//When
		boolean exist = repository.existsMatchPredictionByUserIdAndFixtureId(userId, 17711L);

		//Then
		assertTrue(exist);

		//CleanUp
		try {
			repository.deleteById(matchPredictionId);
			userRepository.deleteById(userId);
		} catch (Exception ex) {
			//do nothing
		}
    }
}
