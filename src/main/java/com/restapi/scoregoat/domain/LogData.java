package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LOG_DATA")
public class LogData {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "Date_Time")
    private LocalDateTime dateTime;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "OPEARTE_VALUE")
    private String operateValue;

    public LogData(Long userId, String operateValue, String description) {
        this.userId = userId;
        this.operateValue = operateValue;
        this.dateTime = LocalDateTime.now();
        this.description = description;
    }
}
