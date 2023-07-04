package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.domain.Ranking;
import com.restapi.scoregoat.domain.UserPredictionDto;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class SortManager {

    public List<Ranking> sortListByPoints(List<Ranking> list) {
        list.sort(Comparator.comparing(Ranking::getPoints).reversed());
        return list;
    }

    public List<Ranking> sortListByRank(List<Ranking> list) {
        list.sort(Comparator.comparing(Ranking::getRank));
        return list;
    }

    public List<UserPredictionDto> sortList(List<UserPredictionDto> list) {
        list.sort(Comparator.comparing(UserPredictionDto::getDate).reversed());
        return list;
    }
}
