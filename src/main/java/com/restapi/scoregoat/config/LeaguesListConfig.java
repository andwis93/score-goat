package com.restapi.scoregoat.config;

import java.util.HashMap;
import java.util.Map;

public class LeaguesListConfig {
    Map<Integer, String> leagueList;

    public Map<Integer, String> createList() {
        leagueList = new HashMap<>();
        leagueList.put(39, "Premier League");
        leagueList.put(140, "La Liga");
        leagueList.put(2, "UEFA Champions League");
        leagueList.put(253, "Major League Soccer");
        return leagueList;
    }
}
