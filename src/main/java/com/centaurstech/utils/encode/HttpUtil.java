package com.centaurstech.utils.encode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

public class HttpUtil {

    /**
     * 创建http查询字符串
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String buildHttpQuery(Map<String, String> data) throws UnsupportedEncodingException {
        String builder = new String();
        for (Map.Entry<String, String> pair : data.entrySet()) {
            builder += URLEncoder.encode(pair.getKey(), "utf-8") + "=" + URLEncoder.encode(pair.getValue(), "utf-8") + "&";
        }
        return builder.substring(0, builder.length() - 1);
    }

    /**
     * 解码http查询字符串
     * @param httpQuery
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> decodeHttpQuery(String httpQuery) throws UnsupportedEncodingException {
        Map<String, String> map = new TreeMap<>();

        for (String s : httpQuery.split("&")) {
            String pair[] = s.split("=");
            String value = "";
            if (pair.length == 2) {
                value = URLDecoder.decode(pair[1], "utf-8");
            }
            map.put(URLDecoder.decode(pair[0], "utf-8"), value);
        }

        return map;
    }

}
