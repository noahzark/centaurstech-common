package com.centaurstech.domain;

import com.centaurstech.utils.StringExtractor;

public class BasicAuthContent {
    String username;
    String password;

    BasicAuthContent(String data) {
        String[] result = StringExtractor.extractBasicAuth(data);
        username = result[0];
        password = result[1];
    }

    public static BasicAuthContent fromString(String data) {
        return new BasicAuthContent(data);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
