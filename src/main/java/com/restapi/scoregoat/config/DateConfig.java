package com.restapi.scoregoat.config;

import lombok.Getter;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;

@Getter
@Component
public class DateConfig {
    private final OffsetDateTime from;
    private final OffsetDateTime to;

    public DateConfig() {
        this.from = OffsetDateTime.now().minusDays(50);
        this.to = OffsetDateTime.now().plusDays(10);
    }
}
