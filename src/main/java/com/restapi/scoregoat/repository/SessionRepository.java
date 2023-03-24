package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByUserId(Long userId);
}
