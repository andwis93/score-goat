package com.restapi.scoregoat.validator;

import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class EmailValidator {

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
    public boolean emailValidator(String email){
        StringBuilder regexPattern = new StringBuilder("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        return patternMatches(email, regexPattern.toString());
    }
}
