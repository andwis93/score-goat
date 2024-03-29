package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Respond {
    USER_NOT_CREATED("User with this name already exists. Please choose different user name and try again."),
    USER_CREATED_OK("User created successfully. Please try to log in."),
    EMAIL_INCORRECT("Wrong email address. Please enter valid email address and try again."),
    USERNAME_EXISTS("User name already exists. Please choose different user name."),
    EMAIL_EXISTS("User with provided email address already exist. Please choose different email address."),
    FIELDS_EMPTY("Pleaser provide all necessary information and try again"),
    USER_LOGGED_IN("User logged in successfully"),
    USER_ID_INCORRECT("User id is incorrect"),
    SESSION_EXPIRED ("Session Expired"),
    USER_NOT_EXIST("User with given email does not exist."),
    TO_MANY_ATTEMPTS("You have reached the maximum number of login attempts. Please try again in approximately: "),
    TO_MANY_ATTEMPTS_LESS_THEN_1H("You have reached the maximum number of login attempts. Account will be unlocked within 2 hour"),
    WRONG_PASSWORD("Password, user name or email is incorrect. Please try again"),
    WRONG_OLD_PASSWORD("Old Password is incorrect"),
    NEW_REPEAT_PASSWORD_DIFFERENT("New password and repeat password are not the same"),
    PASSWORD_CHANGED_OK("Password was changed successfully."),
    ACCOUNT_CHANGED_OK("Account was changed successfully."),
    MATCH_UPLOAD_OK_LEAGUE("Matches were upload successfully --LEAGUE ID: "),
    MATCH_UPLOAD_OK_DATE(" --Up to date: "),
    MATCH_UPLOAD_OK_SEASON(" --For Season: "),
    PREDICTIONS_SAVE_OK("Predictions were saved successfully"),
    USER_DELETED_OK("User deleted successfully"),
    ALL_MATCH_UPLOAD_OK("All Matches were uploaded successfully."),
    PASSWORD_RESET_OK("Password was reset successfully. Please check your email for new password."),
    PASSWORD_RESET_ERROR("Wasn't able to reset password.");

    private final String respond;
}
