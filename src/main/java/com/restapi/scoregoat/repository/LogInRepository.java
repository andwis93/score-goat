package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.LogIn;
import com.restapi.scoregoat.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface LogInRepository extends CrudRepository<LogIn, Long> {
    Optional<LogIn> findByUser(User user);

}
