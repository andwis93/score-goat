package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.LogIn;
import com.restapi.scoregoat.repository.LogInRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class UpdateLogInService {
    private LogInRepository logInRepository;
    public long updateLogInLockedDates(){
        List<LogIn> expiredLocks = logInRepository.findAll().stream()
                .filter(empty -> empty.getLocked() != null)
                .filter(time -> time.getLocked().isBefore(LocalDateTime.now()))
                .toList();
        expiredLocks.forEach(LogIn::removeLocked);
        logInRepository.saveAll(expiredLocks);
        return expiredLocks.size();
    }
}
