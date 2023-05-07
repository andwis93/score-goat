package com.restapi.scoregoat;

import com.restapi.scoregoat.service.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScoreGoatApplicationTests {
	@Autowired
	private MatchService service;

	@Test
	void contextLoads() {

	service.uploadMatchesFromLeagueConfigList();
	}
}
