package com.restapi.scoregoat.manager;

import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class DurationManager {
    public String getDuration(final LocalDateTime time) {
        Duration diff = Duration.between(time, LocalDateTime.now());
        String timeRemain = String.format("%d:%02d:%02d",
                diff.toHours(),
                diff.toMinutesPart(),
                diff.toSecondsPart());
        return timeRemain.replace("-","");
    }
    public boolean checkIfLessThen1H(final LocalDateTime time) {
        return Duration.between(time, LocalDateTime.now()).toHours() < 1;
    }
}
