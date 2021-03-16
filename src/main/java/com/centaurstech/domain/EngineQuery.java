package com.centaurstech.domain;

import com.centaurstech.utils.CommonUtils;
import com.centaurstech.utils.time.TimeCalculator;

import java.util.Map;

/**
 * Engine query form super class
 * Created by Feliciano on 7/5/2017.
 *
 * @author Feliciano.Long
 */
public abstract class EngineQuery {

    public static String CHAT_KEY_SPLITTER = "@@@";
    public static String EXTRA_DATA_SPLITTER = "###";
    private static String UID_KEY = "uid";
    private static String CHANNELID_KEY = "channelId";
    private static String CHAT_KEY = "chat_key";
    private static String CHAT_KEY_NEW_PLATFORM = "chatKey";


    
    
    /**
     * Session chat key <br>
     * @Deprecated use uid
     */
    @Deprecated
    String chatKey;
    
    /**
     * Session chat key
     */
    String uid;

    /**
     * session chat key for new platform, unused
     */
    String chatKeyNew;

    /**
     * Appended extra data in chat key
     */
    String extra;

    /**
     * Query start time
     */
    long beginTime;

    /**
     * Query process duration
     */
    long processTime;

    /**
     * Engine query request params
     */
    Map<String, String> requestParams;

    public EngineQuery() {
        beginTime = TimeCalculator.nowInMillis();
    }

    public EngineQuery(String chat_key) {
        this();
        this.uid = getUidByChatkey(chat_key);
        this.extra = getExtraByChatkey(chat_key);
    }
    
    
    public static String getExtraByChatkey(String chat_key) {
        if (chat_key.contains(CHAT_KEY_SPLITTER)) {
            if (chat_key.contains(EXTRA_DATA_SPLITTER)) {
                String sub = chat_key.substring(chat_key.indexOf(CHAT_KEY_SPLITTER) + CHAT_KEY_SPLITTER.length());
                if (sub.contains(EXTRA_DATA_SPLITTER)) {
                    return sub.substring(0, sub.indexOf(EXTRA_DATA_SPLITTER));
                }
            }
        }
        return null;
    }
    
    public static String getUidByChatkey(String chat_key) {
        String uid = chat_key;
        if (chat_key.contains(CHAT_KEY_SPLITTER)) {
            uid = chat_key.substring(0, chat_key.indexOf(CHAT_KEY_SPLITTER));
        }
        return uid;
    }

    

    public void fillKeyMsg(String uid, String appkey, String chatKey) {
        if (CommonUtils.stringNotEmptyOrNull(uid)) {
            this.chatKey = uid;
        }
        if (CommonUtils.stringNotEmptyOrNull(appkey)) {
            this.extra = appkey;
        }
        if (CommonUtils.stringNotEmptyOrNull(chatKey)) {
            this.chatKeyNew = chatKey;
            if (CommonUtils.stringIsEmptyOrNull(this.chatKey)) {
                this.chatKey = chatKey;
            }
        }
    }

    /**
     * 新平台用
     *
     * @param uid
     * @param appkey
     * @param chatKey 暂时备用
     */
    public EngineQuery(String uid, String appkey, String chatKey) {
        fillKeyMsg(uid,appkey,chatKey);
    }

    public EngineQuery(FormRequest<?, ?> formRequest) {
        this(formRequest.getUid(),formRequest.getChannelId(),formRequest.getChatKey());
    }

    public EngineQuery(Map<String, String> requestParams) {
        this(requestParams.get(CHAT_KEY));
    }

    public EngineQuery(Map<String, String> requestParams, boolean keepRequestParams) {
        this(requestParams);
        if (keepRequestParams) {
            this.requestParams = requestParams;
        }
    }

    /**
     * @Deprecated use uid
     */
    @Deprecated
    public String getChatKey() {
        return chatKey;
    }

    /**
     * @Deprecated use uid
     */
    @Deprecated
    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getQueryTime() {
        return processTime = TimeCalculator.nowInMillis() - beginTime;
    }

    public String getQueryTimeString() {
        return "Query " + chatKey + " takes " + getQueryTime() + "ms";
    }

    /**
     * Check if engine form has a valuable key
     *
     * @param requestParams
     * @param key
     * @return
     */
    public static Boolean hasValue(Map<String, String> requestParams, String key) {
        String result = requestParams.getOrDefault(key, null);
        if (result != null && !result.isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean hasValue(String key) {
        if (requestParams == null) {
            throw new NullPointerException("Request params not initialized!");
        }
        return hasValue(requestParams, key);
    }

    /**
     * Get a value using a series of keys from engine form
     *
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

    public String getStringValue(String key) {
        if (requestParams == null) {
            throw new NullPointerException("Request params not initialized!");
        }
        return getStringValue(requestParams, key);
    }

    /**
     * Get a integer value using a key from engine form
     *
     * @param requestParams
     * @param key
     * @return
     */
    public static Integer getIntegerValue(Map<String, String> requestParams, String key) {
        return getIntegerValue(requestParams, key, -1);
    }

    public Integer getIntegerValue(String key) {
        if (requestParams == null) {
            throw new NullPointerException("Request params not initialized!");
        }
        return getIntegerValue(requestParams, key);
    }

    public static Integer getIntegerValue(Map<String, String> requestParams, String key, Integer defaultValue) {
        if (!requestParams.containsKey(key)) {
            return defaultValue;
        }
        String stringValue = requestParams.get(key);
        try {
            return Integer.valueOf(stringValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Integer getIntegerValue(String key, Integer defaultValue) {
        if (requestParams == null) {
            throw new NullPointerException("Request parameters not initialized!");
        }
        return getIntegerValue(requestParams, key, defaultValue);
    }
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }

}
