package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogInDto {
    private Long id;
    private int attempt;
    private LocalDateTime locked;
    private User user;
}
