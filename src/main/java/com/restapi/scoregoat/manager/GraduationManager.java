package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.repository.GraduationRepository;
import com.restapi.scoregoat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GraduationManager {
    private final GraduationRepository repository;
    private final UserRepository userRepository;

    public void graduationUpdate(MatchPrediction prediction) {
        Graduation graduation;
        User user = userRepository.findById(prediction.getUser().getId()).orElseThrow(IllegalArgumentException::new);
        if (repository.existsGraduationByLeagueAndUserId(prediction.getLeagueId(), user.getId())) {
            graduation = repository.findByLeagueAndUserId(prediction.getLeagueId(), user.getId());
        } else {
            graduation = new Graduation(prediction.getLeagueId(), user);
        }
        graduation.addPoints(prediction.getPoints());
        repository.save(graduation);
    }
}
