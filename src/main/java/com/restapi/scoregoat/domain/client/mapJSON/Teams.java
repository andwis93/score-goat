package com.restapi.scoregoat.domain.client.mapJSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Teams {
    @JsonProperty("home")
    private Team homeTeam;
    @JsonProperty("away")
    private Team awayTeam;
}
