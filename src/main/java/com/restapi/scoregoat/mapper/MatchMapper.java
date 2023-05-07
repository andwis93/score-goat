package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.Match;
import com.restapi.scoregoat.domain.MatchDto;
import com.restapi.scoregoat.domain.client.mapJSON.FixtureRespond;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
public class MatchMapper {

    public MatchDto mapMatchToMatchDto(final Match match) {
        return new MatchDto(
                match.getId(),
                match.getLeagueId(),
                match.getFixtureId(),
                match.getDate().toLocalDate().toString(),
                match.getDate().toLocalTime().toString(),
                match.getStatus(),
                match.getElapsed(),
                match.getHomeTeam(),
                match.getHomeLogo(),
                match.isHomeWinner(),
                match.getAwayTeam(),
                match.getAwayLogo(),
                match.isAwayWinner(),
                match.getHomeGoals(),
                match.getAwayGoals());
    }

    public Match mapFixtureToMatch(final FixtureRespond fixtureRespond) {
        Match match = new Match();
        match.setLeagueId(fixtureRespond.getLeague().getId());
        match.setFixtureId(fixtureRespond.getFixture().getFixtureId());
        match.setDate(OffsetDateTime.parse(fixtureRespond.getFixture().getDateUTS()));
        match.setStatus(fixtureRespond.getFixture().getStatus().getStatus());
        match.setElapsed(fixtureRespond.getFixture().getStatus().getElapsed());
        match.setHomeTeam(fixtureRespond.getTeams().getHomeTeam().getName());
        match.setHomeLogo(fixtureRespond.getTeams().getHomeTeam().getLogo());
        match.setHomeWinner(fixtureRespond.getTeams().getHomeTeam().isWinner());
        match.setAwayTeam(fixtureRespond.getTeams().getAwayTeam().getName());
        match.setAwayLogo(fixtureRespond.getTeams().getAwayTeam().getLogo());
        match.setAwayWinner(fixtureRespond.getTeams().getAwayTeam().isWinner());
        match.setHomeGoals(fixtureRespond.getGoals().getHome());
        match.setAwayGoals(fixtureRespond.getGoals().getAway());
        return match;
    }

    public List<Match> mapFixtureRespondToMatchList(final List<FixtureRespond> fixtureRespondList) {
        return fixtureRespondList.stream()
                .map(this::mapFixtureToMatch).collect(toList());
    }

    public List<MatchDto> mapMatchToMatchDtoList(final List<Match> matchList) {
        return matchList.stream().map(this::mapMatchToMatchDto).collect(toList());
    }
}
