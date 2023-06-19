package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.config.LeaguesListConfig;
import com.restapi.scoregoat.domain.Graduation;
import com.restapi.scoregoat.domain.MatchPrediction;
import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.service.DBService.GraduationDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class GraduationClientService {
    private final LeaguesListConfig config;
    private final GraduationDBService service;
    private final UserDBService userService;

    public void graduationUpdate(MatchPrediction prediction) {
        Graduation graduation;
        User user = userService.findById(prediction.getUser().getId());
        if (user != null) {
            if (service.existsGraduationByLeagueAndUserId(prediction.getLeagueId(), user.getId())) {
                graduation = service.findByLeagueAndUserId(prediction.getLeagueId(), user.getId());
            } else {
                graduation = new Graduation(prediction.getLeagueId(), user);
            }
            graduation.addPoints(prediction.getPoints());
            service.save(graduation);
        } else {
            throw new NullPointerException();
        }
    }

    public void executeRankAssign() {
        Map<Integer, String> leagueList = config.getLeagueList();
        leagueList.keySet().forEach(leagueId -> {
            List<Graduation> graduationList = sortList(service.findByLeagueId(leagueId));
            rankAssign(graduationList);
            service.saveAll(graduationList);
        });
    }

    private List<Graduation> sortList(List<Graduation> list) {
        list.sort(Comparator.comparing(Graduation::getPoints).reversed());
        return list;
    }

    private void rankAssign(List<Graduation> graduationList) {
        int rank = 1;
        graduationList.get(0).setRank(rank);
        for(int i = 1; i < graduationList.size(); i++) {
            if (graduationList.get(i).getPoints() != graduationList.get(i - 1).getPoints()) {
                rank++;
            }
            graduationList.get(i).setRank(rank);
        }
    }
}
