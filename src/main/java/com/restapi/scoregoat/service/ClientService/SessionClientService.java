package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.domain.Code;
import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.domain.Session;
import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.service.DBService.LogDataDBService;
import com.restapi.scoregoat.service.DBService.SessionDBService;
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
public class SessionClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionClientService.class);
    private final SessionDBService service;
    private final LogDataDBService logDataService;

    public long removeExpiredSession() {
        List<Session> expiredSession = service.findAll().stream()
                .filter(sessionEnd -> sessionEnd.getEnd().isBefore(LocalDateTime.now()))
                .toList();
        service.deleteAll(expiredSession);
        return expiredSession.size();
    }

    public void refreshSession(Session session) {
        session.setEnd(LocalDateTime.now());
    }

    public boolean saveRefreshedSession(final User user) {
        try{
            Session session = service.findByUserId(user.getId());
            session.setUser(user);
            refreshSession(session);
            service.save(session);
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
