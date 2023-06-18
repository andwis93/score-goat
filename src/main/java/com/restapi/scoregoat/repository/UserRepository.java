package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    List<User> findAllByEmail(String email);
    List<User> findAllByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
