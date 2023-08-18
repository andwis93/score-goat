package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Active;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ActiveRepository extends CrudRepository<Active, Long> {
    Active findByLeagueId(int leagueId);
}
