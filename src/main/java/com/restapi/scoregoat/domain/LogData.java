package com.restapi.scoregoat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(force = true)
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
    @Column(name = "CODE")
    private String code;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "OPEARTE_VALUE")
    private String operateValue;

    public LogData(Long userId, String operateValue, String code, String description) {
        this.userId = userId;
        this.operateValue = operateValue;
        this.dateTime = LocalDateTime.now();
        this.code = code;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogData logData = (LogData) o;
        return id.equals(logData.id) && Objects.equals(dateTime, logData.dateTime) &&
                Objects.equals(code, logData.code) && Objects.equals(description, logData.description) &&
                Objects.equals(userId, logData.userId) && Objects.equals(operateValue, logData.operateValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, code, description, userId, operateValue);
    }
}
