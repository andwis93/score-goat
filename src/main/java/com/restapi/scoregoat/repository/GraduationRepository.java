package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Graduation;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface GraduationRepository extends CrudRepository<Graduation, Long> {
}
