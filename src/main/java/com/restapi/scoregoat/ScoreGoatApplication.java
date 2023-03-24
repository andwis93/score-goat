package com.restapi.scoregoat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ScoreGoatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScoreGoatApplication.class, args);
	}

}
