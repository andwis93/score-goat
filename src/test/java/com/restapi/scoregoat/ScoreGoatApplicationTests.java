package com.restapi.scoregoat;

import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.service.MatchPredictionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScoreGoatApplicationTests {
	@Autowired
	private MatchPredictionService service;
//	private ScoreGoatFacade facade;

	@Test
	void contextLoads() {
		service.graduatePredictions();
	//	facade.uploadMatchesFromLeagueConfigList();
	}
}
