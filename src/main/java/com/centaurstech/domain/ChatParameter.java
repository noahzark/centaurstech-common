package com.centaurstech.domain;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fangzhou.Long on 6/20/2020
 * @project common
 */
public class ChatParameter {
    ChatApp chatApp;

    String uid;
    String nickname;

    String ask;

    boolean newSession;

    GPSLocation geo;

    Map<String, String> headers;

    JSONObject extraInfo;

    String sn;

    private ChatParameter() {
        headers = new HashMap<>();
    }

    public ChatParameter(ChatApp chatApp, String uid, String ask) {
        this();
        this.chatApp = chatApp;
        this.uid = uid;
        this.ask = ask;
    }

    public ChatParameter(String appkey, String appsecret, String uid, String ask) {
        this(new ChatApp(appkey, appsecret), uid, ask);
    }

    public String calVerify(String now) {
        return chatApp.calVerify(uid, now);
    }

    public ChatApp getChatApp() {
        return chatApp;
    }

    public ChatParameter setChatApp(ChatApp chatApp) {
        this.chatApp = chatApp;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public ChatParameter setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public ChatParameter setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getAsk() {
        return ask;
    }

    public ChatParameter setAsk(String ask) {
        this.ask = ask;
        return this;
    }

    public boolean isNewSession() {
        return newSession;
    }

    public ChatParameter setNewSession(boolean newSession) {
        this.newSession = newSession;
        return this;
    }

    public GPSLocation getGeo() {
        return geo;
    }

    public ChatParameter setGeo(GPSLocation geo) {
        this.geo = geo;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public ChatParameter setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public JSONObject getExtraInfo() {
        return extraInfo;
    }

    public ChatParameter setExtraInfo(JSONObject extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
