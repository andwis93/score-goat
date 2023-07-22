package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Session;
import com.restapi.scoregoat.domain.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    @Override
    @NotNull
    List<Session> findAll();
    Optional<Session> findByUserId(Long userId);
    boolean existsByUser(User user);
}
