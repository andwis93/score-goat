package com.restapi.scoregoat.domain.client.mapJSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixtureRespond {
    @JsonProperty("fixture")
    private Fixture fixture;
    @JsonProperty("league")
    private LeagueAPI league;
    @JsonProperty("teams")
    private Teams teams;
    @JsonProperty("goals")
    private Goals goals;
}
