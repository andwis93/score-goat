package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.Session;
import com.restapi.scoregoat.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class SessionService {
    private SessionRepository repository;

    public long removeExpiredSession() {
        List<Session> expiredSession = repository.findAll().stream()
                .filter(sessionEnd -> sessionEnd.getEnd().isBefore(LocalDateTime.now()))
                .toList();
        repository.deleteAll(expiredSession);
        return expiredSession.size();
    }
}
