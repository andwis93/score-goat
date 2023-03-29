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

    public UserRespondDto(String respond) {
        this.respond = respond;
    }

    public UserRespondDto setExtendResponse(final User user, String respond) {
        return new UserRespondDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                true,
                respond);
    }
}
