package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRespondDto {
    private Long id;
    private String userName;
    private String email;
    private boolean isLogIn = false;
    private String respond;
    private String notificationType;

    public UserRespondDto(String respond, String notificationType) {
        this.respond = respond;
        this.notificationType = notificationType;
    }

    public UserRespondDto setExtendResponse(final User user, String respond, String notificationType) {
        return new UserRespondDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                true,
                respond,
                notificationType
                );
    }

    public UserRespondDto setShortRespond(String name, String email, String respond, String notificationType) {
        return new UserRespondDto(
               null,
                name,
                email,
                false,
                respond,
                notificationType
        );
    }
}
