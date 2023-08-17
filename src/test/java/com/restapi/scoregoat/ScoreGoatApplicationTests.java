package com.restapi.scoregoat;

import com.restapi.scoregoat.facade.ScoreGoatFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScoreGoatApplicationTests {
	@Autowired
	private ScoreGoatFacade facade;

	@Test
	void contextLoads() {
//			facade.rankPredictions();
//			facade.assignRanks();
//			facade.uploadMatchesFromLeagueConfigList();

	}

}
