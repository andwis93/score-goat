package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.Session;
import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.repository.SessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class SessionDBService {
    private final SessionRepository repository;

   public List<Session> findAll() {
       return repository.findAll();
   }

   public Session findByUserId(Long userId) {
       return repository.findByUserId(userId).orElse(new Session());
   }

   public void save(Session session) {
       repository.save(session);
   }

   public void deleteAll(List<Session> sessions) {
     repository.deleteAll(sessions);
   }
   public boolean checkIfSessionExistsByUser(User user) {
       return repository.existsByUser(user);
   }
}
