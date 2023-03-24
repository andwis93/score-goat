package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.LogData;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
@Repository
public interface LogDataRepository extends CrudRepository<LogData, Long> {
    @Override
    @NotNull
    List<LogData> findAll();
}
