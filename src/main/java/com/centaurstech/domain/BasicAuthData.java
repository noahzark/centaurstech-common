package com.centaurstech.domain;

import com.centaurstech.utils.StringExtractor;

public class BasicAuthData {
    String username;
    String password;

    BasicAuthData(String data) {
        String[] result = StringExtractor.extractBasicAuth(data);
        username = result[0];
        password = result[1];
    }

    public static BasicAuthData fromString(String data) {
        return new BasicAuthData(data);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
