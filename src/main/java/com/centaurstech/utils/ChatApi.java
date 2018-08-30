package com.centaurstech.utils;

import com.centaurstech.domain.GPSLocation;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

import static com.centaurstech.utils.QueryHelper.urlEncodeUTF8;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class ChatApi {

    private OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    String server;

    public ChatApi(String server) {
        this.server = server;
    }

    private Request.Builder buildRequest(Map<String, String> headers, Map<String, String> queries) {
        String queryParameters = "";
        if (queries != null && queries.size() > 0) {
            queryParameters = "?" + queries.entrySet().stream()
                    .map(p -> urlEncodeUTF8(p.getKey()) + "=" + urlEncodeUTF8(p.getValue()))
                    .reduce((p1, p2) -> p1 + "&" + p2)
                    .orElse("");
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(server + queryParameters);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return requestBuilder;
    }

    private String getForString(Map<String, String> headers, Map<String, String> queries) throws IOException {
        Request request = buildRequest(headers, queries)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        // System.out.println(res);

        return res;
    }

    private JSONObject getForJSON(Map<String, String> headers, Map<String, String> queries) throws IOException {
        String resStr = getForString(headers, queries);
        try {
            JSONObject resJson = new JSONObject(resStr);
            if(resJson != null && resJson.has("retcode")){
                if(0 == resJson.getInt("retcode")){
                    return resJson;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String postForString(RequestBody body) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("cache-control", "no-cache");

        Request request = buildRequest(headers, null)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        // System.out.println(res);

        return res;
    }

    private JSONObject postForJSON(RequestBody body) throws IOException {
        String resStr = postForString(body);
        try {
            JSONObject resJson = new JSONObject(resStr);
            if(resJson != null && resJson.has("retcode")){
                if(0 == resJson.getInt("retcode")){
                    return resJson;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject chat(String appkey, String appsecret,
                                       String uid, String nickname, String ask) throws IOException {
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

    public String sendJson(String queryResultType, JSONObject jsonObject, String serverSalt) throws IOException {
        String ticket = UUID.randomUUID().toString();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = Long.toString(timestamp.getTime());
        jsonObject.put("search_result_type", queryResultType);

        FormBody body = new FormBody.Builder()
                .add("data", jsonObject.toString())
                .add("key", ticket)
                .add("timestamp", time)
                .add("secret", Md5.digest(time + serverSalt))
                .add("resulttype", queryResultType)
                .build();

        JSONObject resJson = postForJSON(body);
        if (resJson != null) {
            return ticket;
        }
        return null;
    }

    public String sendGPS(String appkey, String appsecret, String uid, GPSLocation location) throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = Long.toString(timestamp.getTime());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appkey", appkey);
        jsonObject.put("uid", uid);
        jsonObject.put("timestamp", time);
        jsonObject.put("geo[lat]", location.getLat());
        jsonObject.put("geo[lng]", location.getLng());
        jsonObject.put("verify", Md5.digest(appsecret + uid + time));

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        JSONObject resJson = postForJSON(body);
        if (resJson != null && resJson.has("msg")) {
            return resJson.getString("msg");
        }

        return null;
    }

    public GPSLocation getGPS(String uid, String salt) throws IOException {
        return getGPS(uid, salt, null);
    }

    public GPSLocation getGPS(String uid, String salt, GPSLocation defaultLocation) throws IOException {
        GPSLocation geoInfo = defaultLocation;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = Long.toString(timestamp.getTime());

        HashMap<String, String> request = new HashMap<>();
        request.put("chatkey", uid);
        request.put("timestamp", time);
        request.put("secret", Md5.digest(time + salt));

        JSONObject resJson = getForJSON(request, null);
        if (resJson != null && resJson.has("geo")) {
            JSONObject geo = resJson.getJSONObject("geo");
            if (geo.has("lat")) {
                if (geo.get("lat").getClass() == Double.class) {
                    geoInfo = new GPSLocation(
                            geo.getDouble("lat"),
                            geo.getDouble("lng"));
                } else if (geo.get("lat").getClass() == Integer.class) {
                    geoInfo = new GPSLocation(
                            geo.getInt("lat"),
                            geo.getInt("lng"));
                } else if (geo.get("lat").getClass() == String.class) {
                    try {
                        geoInfo = new GPSLocation(
                                Double.valueOf(geo.getString("lat")),
                                Double.valueOf(geo.getString("lng")));
                    } catch (Exception e) {
                        return geoInfo;
                    }
                }
            }
        }

        return geoInfo;
    }

    public String engineChat(String action, Map<String, String> data) throws IOException {
        FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("action", action)
                .add("format", "json");
        for (String entry : data.keySet()) {
            bodyBuilder.add(entry, data.get(entry));
        }
        FormBody body = bodyBuilder.build();

        String resStr = postForString(body);
        return resStr;
    }

    public JSONObject engineChatJson(String action, Map<String, String> data) throws IOException {
        String resStr = engineChat(action, data);
        JSONObject resJson = null;
        try {
            resJson = new JSONObject(resStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resJson;
    }

}
