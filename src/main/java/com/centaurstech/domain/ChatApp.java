package com.centaurstech.domain;

import com.centaurstech.utils.encode.Md5;

public class ChatApp {

    String appkey;
    String appsecret;

    public ChatApp(String appkey, String appsecret) {
        this.appkey = appkey;
        this.appsecret = appsecret;
    }

    public String getVerify(String uid, String timestamp) {
        String verify = Md5.digest(appsecret + uid + timestamp);
        return verify;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String calVerify(String uid, String now) {
        return Md5.digest(appsecret + uid + now);
    }

}
