package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Graduation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface GraduationRepository extends JpaRepository<Graduation, Long> {
    Graduation findByLeagueAndUserId(int leagueId, Long userid);
  //  List<Graduation> findByLeagueId(int leagueId);
    boolean existsGraduationByLeagueAndUserId(int leagueId, Long userId);
}
