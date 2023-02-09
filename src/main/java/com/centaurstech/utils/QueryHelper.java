package com.centaurstech.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

public class QueryHelper {

    public static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static String urlDecodeUTF8(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    public static Map<String, String> urlDecodeUTF8Map(String httpQuery) {
        Map<String, String> map = new TreeMap<>();

        for (String s : httpQuery.split("&")) {
            try {
                String pair[] = s.split("=");
                String value = "";
                if (pair.length == 2) {
                    value = URLDecoder.decode(pair[1], "utf-8");
                }
                map.put(URLDecoder.decode(pair[0], "utf-8"), value);
            } catch (UnsupportedEncodingException e) { }
        }

        return map;
    }

}
