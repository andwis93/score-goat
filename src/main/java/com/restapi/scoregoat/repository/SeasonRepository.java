package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Season;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends CrudRepository<Season, Long> {

    @Override
    Season save( Season season);
}
