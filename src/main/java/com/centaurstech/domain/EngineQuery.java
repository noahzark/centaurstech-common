package com.centaurstech.domain;

import com.centaurstech.utils.TimeCalculator;

import java.util.Map;

/**
 * Engine query form super class
 * Created by Feliciano on 7/5/2017.
 */
public abstract class EngineQuery {

    /**
     * Session chat key
     */
    String chatKey;

    /**
     * Query start time
     */
    long beginTime;

    /**
     * Query process duration
     */
    long processTime;

    public EngineQuery() {
        beginTime = TimeCalculator.nowInMillis();
    }

    public EngineQuery(String chat_key) {
        this();
        chatKey = chat_key;
        if (chatKey.contains("@@@")) {
            chatKey = chatKey.substring(0, chatKey.indexOf("@@@"));
        }
    }

    public EngineQuery(Map<String,String> requestParams) {
        this(requestParams.get("chat_key"));
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public long getQueryTime() {
        return processTime = TimeCalculator.nowInMillis() - beginTime;
    }

    public String getQueryTimeString() {
        return "Query " + chatKey + " takes " + getQueryTime() + "ms";
    }

    /**
     * Check if engine form has a valuable key
     * @param requestParams
     * @param key
     * @return
     */
    public static Boolean hasValue(Map<String,String> requestParams, String key) {
        String result = requestParams.getOrDefault(key, null);
        if (result != null && !result.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Get a value using a series of keys from engine form
     * @param requestParams
     * @param keys
     * @return
     */
    public static String getStringValue(Map<String, String> requestParams, String... keys) {
        for (String key : keys) {
            if (hasValue(requestParams, key)) {
                return requestParams.get(key);
            }
        }
        return "";
    }

}
