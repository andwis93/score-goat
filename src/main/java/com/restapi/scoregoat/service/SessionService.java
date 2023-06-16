package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.Code;
import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.domain.Session;
import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class SessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);
    private final SessionRepository repository;
    private final LogDataService logDataService;

    public long removeExpiredSession() {
        List<Session> expiredSession = repository.findAll().stream()
                .filter(sessionEnd -> sessionEnd.getEnd().isBefore(LocalDateTime.now()))
                .toList();
        repository.deleteAll(expiredSession);
        return expiredSession.size();
    }

    public void refreshSession(Session session) {
        session.setEnd(LocalDateTime.now());
    }

    public boolean saveRefreshedSession(final User user) {
        try{
            Session session = repository.findByUserId(user.getId()).orElse(new Session());
            session.setUser(user);
            refreshSession(session);
            repository.save(session);
            return true;

        } catch (IllegalArgumentException ex) {
            String message = ex.getMessage() + "  --ERROR: Couldn't execute \"SaveRefreshedSession\"-- ";
            logDataService.saveLog(new LogData(
                    null,"With UserID: " + user.getId(), Code.SESSION_REFRESH_ERROR.getCode(), message));
            LOGGER.error(message,ex);
            return false ;
        }
    }
}
