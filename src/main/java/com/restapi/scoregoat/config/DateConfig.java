package com.restapi.scoregoat.config;

import lombok.Getter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Component
public class DateConfig {
    private final OffsetDateTime from;
    private final OffsetDateTime to;
    private final LocalDate unActiveUser;

    public DateConfig() {
        this.from = OffsetDateTime.now().minusDays(0);
        this.to = OffsetDateTime.now().plusDays(10);
        this.unActiveUser = LocalDate.now().minusYears(1);
    }
}
