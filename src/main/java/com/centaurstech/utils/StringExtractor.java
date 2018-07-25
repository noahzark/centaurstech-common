package com.centaurstech.utils;

import com.centaurstech.domain.BasicAuthContent;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringExtractor {

    public final static String GUEST_AUTH_PATTERN = "^Guest\\s(\\w+={0,2})";
    public final static String BASIC_AUTH_PATTERN = "^Basic\\s(\\w+={0,2})";

    public final static String BEARER_TOKEN_PATTERN = "^Bearer\\s([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})";

    public final static String FACEBOOK_AUTH_PATTERN = "^Facebook\\s(\\w+={0,2})";

    private static Matcher match(String data, String patternStr){
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(data);
        if(!matcher.matches()){
            throw new IllegalStateException("No match found "+ patternStr );
        }
        return matcher;
    }

    private static String[] extractBase64(String data, String pattern){
        Matcher matcher = match(data,pattern);
        String b64authString = matcher.group(1);
        byte[] authBytes = Base64.getDecoder().decode(b64authString);
        String authString = new String(authBytes);
        return authString.split(":");
    }

    public static String extractAuthToken(String data){
        Matcher matcher = match(data, BEARER_TOKEN_PATTERN);
        return matcher.group(1);
    }

    public static String[] extractBasicAuth(String data) {
        return extractBase64(data, BASIC_AUTH_PATTERN);
    }

    public static String[] extractFacebookAuth(String authHeaderStr) {
        return extractBase64(authHeaderStr, FACEBOOK_AUTH_PATTERN);
    }
}