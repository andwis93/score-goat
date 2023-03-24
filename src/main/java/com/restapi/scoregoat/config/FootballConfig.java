package com.restapi.scoregoat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class FootballConfig {
    @Value("${football.api.endpoint.prod}")
    private String footballApiEndpoint;
    @Value("${football.app.key}")
    private String footballAppKey;
    @Value("${football.app.header}")
    private String footballAppHeader;
}
