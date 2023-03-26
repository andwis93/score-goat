package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Respond {
    USER_NOT_CREATED("User with this name already exists. Please choose different user name and try again."),
    USER_CREATED_OK("User created successfully."),
    EMAIL_INCORRECT("Wrong email address. Please enter valid email address and try again."),
    USERNAME_EXISTS("User name already exists. Please choose different user name."),
    EMAIL_EXISTS("User with provided email address already exists. Please choose different email address."),
    FIELDS_EMPTY("Pleaser provide all necessary information and try again"),
    USER_LOGGED_IN("User logged in successfully"),

    USER_NOT_EXIST("User with this name or email does not exist."),
    TO_MANY_ATTEMPTS("You have reached the maximum number of login attempts. Please try again in approximately: "),
    TO_MANY_ATTEMPTS_LESS_THEN_1H("You have reached the maximum number of login attempts. Account will be unlocked within 2 hour"),
    WRONG_PASSWORD("Password, user name or email is incorrect. Please try again"),
    WRONG_OLD_PASSWORD("Old Password is incorrect"),
    NEW_REPEAT_PASSWORD_DIFFERENT("New password and repeat password are not the same"),
    PASSWORD_CHANGED_OK("Password was changed successfully.");

    private final String respond;
}
