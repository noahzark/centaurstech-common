package com.centaurstech.utils;

import okhttp3.*;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class ChatApi {

    private OkHttpClient client = new OkHttpClient();

    String server;

    public ChatApi(String server) {
        this.server = server;
    }

    private String postForString(FormBody body) throws Exception {
        Request request = new Request.Builder()
                .url(server)
                .post(body)
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();

        return res;
    }

    public JSONObject chat(String appkey, String appsecret,
                                       String uid, String nickname, String ask) throws Exception {
        // Prepare verify string
        String now = (new Date()).getTime() + "";
        String verify = Md5.digest(appsecret + uid + now);

        // Generate request body
        FormBody body = new FormBody.Builder()
                .add("appkey", appkey)
                .add("uid", uid)
                .add("timestamp",now)
                .add("verify", verify)
                .add("nickname", nickname)
                .add("msg", ask)
                .build();

        String res = postForString(body);
        JSONObject result = new JSONObject(res);
        /*
        if (json.has("data"))
            System.out.println("Appended data: " + json.getJSONObject("data"));
        else
            System.out.println("There is no extra data");
        */
        return result;
    }

    public String sendJson(String queryResultType, JSONObject jsonObject) throws Exception {
        String ticket = UUID.randomUUID().toString();

        String SERVER_SALT = " QUERY_RESULT_SALT";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = Long.toString(timestamp.getTime());
        jsonObject.put("search_result_type", queryResultType);

        FormBody body = new FormBody.Builder()
                .add("data", jsonObject.toString())
                .add("key", ticket)
                .add("timestamp",time)
                .add("secret",Md5.digest(time + SERVER_SALT))
                .add("resulttype", queryResultType)
                .build();

        String resStr = postForString(body);
        JSONObject resJson = new JSONObject(resStr);
        if(resJson.has("retcode")){
            if(0 == resJson.getInt("retcode")){
                return ticket;
            }
        }
        return null;
    }

}
