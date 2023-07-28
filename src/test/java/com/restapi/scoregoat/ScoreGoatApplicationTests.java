package com.restapi.scoregoat;

import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.manager.PasswordManager;
import com.restapi.scoregoat.service.ClientService.UserClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

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
