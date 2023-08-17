package com.restapi.scoregoat.manager;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class CodeManager {
    private static final String CHAR ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-$!@#%&";

    private static final SecureRandom random = new SecureRandom();
    public String generateRandomString(int size) {
        StringBuilder result = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(CHAR.length());
            result.append(CHAR.charAt(index));
        }
        return shuffleString(result.toString());
    }

    private static String shuffleString(String input) {
        List<String> result = Arrays.asList(input.split(""));
        Collections.shuffle(result);
        return String.join("", result);
    }
}
