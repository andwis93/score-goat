package com.restapi.scoregoat.config;

import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class LeaguesListConfig {
    Map<Integer, String> leagueList;

    public LeaguesListConfig() {
        this.leagueList = createList();
    }

    public Map<Integer, String> createList() {
        leagueList = new HashMap<>();
        leagueList.put(39, "Premier League");
        leagueList.put(140, "La Liga");
        leagueList.put(2, "UEFA Champions League");
        leagueList.put(253, "Major League Soccer");
        return leagueList;
    }
}
