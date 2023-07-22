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
    private final SessionDBService dbService;
    private final LogDataDBService logDataService;

    public long removeExpiredSession() {
        List<Session> expiredSession = dbService.findAll().stream()
                .filter(sessionEnd -> sessionEnd.getEnd().isBefore(LocalDateTime.now()))
                .toList();
        dbService.deleteAll(expiredSession);
        return expiredSession.size();
    }

    public void setSessionEndTime(Session session) {
        session.setEnd(LocalDateTime.now());
    }

    public boolean createSession(User user) {
        try{
            Session session = dbService.findByUserId(user.getId());
            session.setUser(user);
            setSessionEndTime(session);
            dbService.save(session);
            return true;

        } catch (IllegalArgumentException ex) {
            StringBuilder message = new StringBuilder(ex.getMessage() + "  --ERROR: Couldn't execute \"SaveRefreshedSession\"-- ");
            logDataService.saveLog(new LogData(
                    null,"With UserID: " + user.getId(), Code.SESSION_REFRESH_ERROR.getCode(), message.toString()));
            LOGGER.error(message.toString(),ex);
            return false ;
        }
    }

    public boolean refreshSession(User user) {
        Session session = dbService.findByUserId(user.getId());
        if (session.getUser() != null) {
            setSessionEndTime(session);
            dbService.save(session);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfSessionExistsByUser(User user) {
        return dbService.checkIfSessionExistsByUser(user);
    }

}
