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
    private int windowStatus;

    public UserRespondDto(String respond, int windowStatus) {
        this.respond = respond;
        this.windowStatus = windowStatus;
    }
}
