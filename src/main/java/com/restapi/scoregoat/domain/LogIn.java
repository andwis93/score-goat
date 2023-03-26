package com.restapi.scoregoat.domain;

import com.restapi.scoregoat.repository.LogInRepository;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LOG_IN_ATTEMPT")
public class LogIn {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "ATTEMPTS")
    private int attempt;
    @Column(name = "LOCKED_TILL")
    private LocalDateTime locked;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public LogIn(User user) {
        this.attempt = 0;
        this.user = user;
    }

    public void addAttempt() {
        this.attempt += 1;
    }

    public void resetAttempt() {
        this.attempt = 0;
    }

    public void removeLocked() {
        this.locked = null;
        resetAttempt();
    }
}
