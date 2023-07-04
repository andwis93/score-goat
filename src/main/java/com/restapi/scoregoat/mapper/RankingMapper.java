package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.Ranking;
import com.restapi.scoregoat.domain.RankingDto;
import org.springframework.stereotype.Service;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
public class RankingMapper {

    public RankingDto mapRankingToRankingDto(Ranking ranking) {
    return new RankingDto(
            Integer.toString(ranking.getRank()),
            ranking.getUser().getName(),
            Integer.toString(ranking.getPoints()),
            ranking.getStatus()
    );}

    public List<RankingDto> mapRankingToRankingDtoList(final List<Ranking> rankings) {
        return rankings.stream().map(this::mapRankingToRankingDto).collect(toList());
    }
}
