package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Graduation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Transactional
@Repository
public interface GraduationRepository extends JpaRepository<Graduation, Long> {
    Graduation findByLeagueIdAndUserId(int leagueId, Long userid);
    List<Graduation> findByLeagueId(int id);
    boolean existsGraduationByLeagueIdAndUserId(int leagueId, Long userId);
}
