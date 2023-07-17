package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RankingDto {
    String rank;
    String userName;
    String points;
    int status;
    int counter;
    int last;
}
