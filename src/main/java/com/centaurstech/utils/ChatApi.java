package com.centaurstech.utils;

import okhttp3.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class ChatApi {

    private OkHttpClient client = new OkHttpClient();
    private MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

    String server;

    public ChatApi(String server) {
        this.server = server;
    }

    public JSONObject chat(String appkey, String appsecret,
                                       String uid, String nickname, String ask) throws Exception {
        // Prepare verify string
        String now = (new Date()).getTime() + "";
        String verify = Md5.digest(appsecret + uid + now);

        // Generate request body
        String bodyStr = String.format(
                "appkey=%s&timestamp=%s&uid=%s&verify=%s&msg=%s&nickname=%s",
                appkey, now, uid, verify, ask, nickname);

        RequestBody body = RequestBody.create(mediaType,
                bodyStr);
        Request request = new Request.Builder()
                .url(server)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        // System.out.println(res);

        JSONObject result = new JSONObject(res);
        /*
        if (json.has("data"))
            System.out.println("Appended data: " + json.getJSONObject("data"));
        else
            System.out.println("There is no extra data");
        */
        return result;
    }

}
