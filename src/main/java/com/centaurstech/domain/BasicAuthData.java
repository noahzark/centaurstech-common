package com.centaurstech.domain;

import com.centaurstech.utils.StringExtractor;

public class BasicAuthData {
    String username;
    String password;

    BasicAuthData(String data) {
        String[] result = StringExtractor.extractBasicAuth(data);
        username = result[0];
        if (result.length == 2) {
            password = result[1];
        } else {
            password = "";
        }
    }

    public static BasicAuthData fromString(String data) {
        try {
            return new BasicAuthData(data);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
