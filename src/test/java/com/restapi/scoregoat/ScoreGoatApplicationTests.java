package com.restapi.scoregoat;

import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.service.ClientService.ActiveClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScoreGoatApplicationTests {
	@Autowired
	private ScoreGoatFacade facade;
	@Autowired
	private ActiveClientService service;

	@Test
	void contextLoads() {
			facade.rankPredictions();
			facade.assignRanks();
//			facade.uploadMatchesFromLeagueConfigList();
//		service.setActive(true, 2);

	}

}
