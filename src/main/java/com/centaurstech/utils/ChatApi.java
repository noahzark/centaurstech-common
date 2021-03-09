package com.centaurstech.utils;

import com.centaurstech.domain.ChatApp;
import com.centaurstech.domain.ChatParameter;
import com.centaurstech.domain.GPSLocation;
import com.centaurstech.utils.encode.Md5;
import com.centaurstech.utils.http.RequestBodyUtil;
import com.centaurstech.utils.http.SimpleHttpClient;
import com.centaurstech.utils.time.TimeCalculator;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Feliciano on 7/3/2018.
 * @author feli
 */
public class ChatApi extends SimpleHttpClient {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType OCTECT_STREAM
            = MediaType.parse("application/octet-stream");

    final static Pattern AUDIO_MIME_CODEC_RATE_PATTERN = Pattern.compile("(codec=(?<codec>pcm|amr|wav))(;)?(rate=(?<rate>8000|16000))?");

    public ChatApi(String server) {
        super(server);
    }

    public ChatApi(String server, long readTimeout) {
        super(server, readTimeout);
    }

    /**
     * Call chat in old style
     *
     * @param chatApp
     * @param uid
     * @param nickname
     * @param ask
     * @return
     * @throws IOException
     * @deprecated use {@link #chat(ChatParameter)} instead.
     */
    @Deprecated
    public JSONObject chat(ChatApp chatApp, String uid, String nickname, String ask) throws IOException {
        return this.chat(chatApp.getAppkey(), chatApp.getAppsecret(),
                                uid, nickname, ask);
    }

    /**
     *
     * @param chatApp
     * @param uid
     * @param nickname
     * @param ask
     * @param newSession
     * @return
     * @throws IOException
     * @deprecated use {@link #chat(ChatParameter)} instead.
     */
    @Deprecated
    public JSONObject chat(ChatApp chatApp, String uid, String nickname, String ask, boolean newSession) throws IOException {
        return this.chat(chatApp.getAppkey(), chatApp.getAppsecret(),
                uid, nickname, ask, newSession);
    }

    /**
     *
     * @param chatApp
     * @param uid
     * @param nickname
     * @param ask
     * @param newSession
     * @param headers
     * @return
     * @throws IOException
     * @deprecated use {@link #chat(ChatParameter)} instead.
     */
    @Deprecated
    public JSONObject chat(ChatApp chatApp, String uid, String nickname, String ask, boolean newSession, Map<String, String> headers) throws IOException {
        return this.chat(chatApp.getAppkey(), chatApp.getAppsecret(),
                uid, nickname, ask, newSession, headers);
    }

    /**
     *
     * @param appkey
     * @param appsecret
     * @param uid
     * @param nickname
     * @param ask
     * @return
     * @throws IOException
     * @deprecated use {@link #chat(ChatParameter)} instead.
     */
    @Deprecated
    public JSONObject chat(String appkey, String appsecret,
                           String uid, String nickname, String ask) throws IOException {
        return chat(appkey, appsecret, uid, nickname, ask, false);
    }

    /**
     *
     * @param appkey
     * @param appsecret
     * @param uid
     * @param nickname
     * @param ask
     * @param newSession
     * @return
     * @throws IOException
     * @deprecated use {@link #chat(ChatParameter)} instead.
     */
    @Deprecated
    public JSONObject chat(String appkey, String appsecret,
                           String uid, String nickname, String ask, boolean newSession) throws IOException {
        return chat(appkey, appsecret, uid, nickname, ask, newSession, new HashMap<>());
    }

    /**
     *
     * @param appkey
     * @param appsecret
     * @param uid
     * @param nickname
     * @param ask
     * @param newSession
     * @param headers
     * @return
     * @throws IOException
     * @deprecated use {@link #chat(ChatParameter)} instead.
     */
    @Deprecated
    public JSONObject chat(String appkey, String appsecret,
                           String uid, String nickname, String ask, boolean newSession,
                           Map<String, String> headers) throws IOException {
        ChatParameter chatParameter = new ChatParameter(appkey, appsecret, uid, ask);
        chatParameter.setNickname(nickname)
                .setNewSession(newSession)
                .setHeaders(headers);
        return chat(chatParameter);
    }

    /**
     * Call chat api
     * @param chatParameter
     * @return
     * @throws IOException
     */
    public JSONObject chat(ChatParameter chatParameter) throws IOException {
        // Prepare verify string
        String now = String.valueOf(TimeCalculator.nowInMillis());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appkey", chatParameter.getChatApp().getAppkey());
        jsonObject.put("uid", chatParameter.getUid());
        jsonObject.put("timestamp",now);
        jsonObject.put("verify", chatParameter.calVerify(now));
        jsonObject.put("nickname", chatParameter.getNickname());
        jsonObject.put("msg", chatParameter.getAsk());
        jsonObject.put("new_session", String.valueOf(chatParameter.isNewSession()));
        jsonObject.put("extra_info", chatParameter.getExtraInfo());
        if (chatParameter.getGeo() != null) {
            jsonObject.put("geo[lat]", chatParameter.getGeo().getLat());
            jsonObject.put("geo[lng]", chatParameter.getGeo().getLng());
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        String res;
        if (isSingleApiServer) {
            res = postForString(body, chatParameter.getHeaders(), null);
        } else {
            res = postForString("/api/chat", chatParameter.getHeaders(), body, null);
        }

        return new JSONObject(res);
    }

    public JSONObject voiceChat(ChatApp chatApp, String uid, String nickname, File file, String fileMime) throws IOException {
        return this.voiceChat(chatApp.getAppkey(), chatApp.getAppsecret(), uid, nickname, file, fileMime);
    }

    public JSONObject voiceChat(String appkey, String appsecret, String uid, String nickname, File file, String fileMime) throws IOException {
        HashMap<String, String> queries = generateQuery(appkey, appsecret, uid, nickname);
        Matcher matcher = AUDIO_MIME_CODEC_RATE_PATTERN.matcher(fileMime);
        if (matcher.find()) {
            String codec = matcher.group("codec");
            if (codec != null) { queries.put("codec", codec); };
            String rate = matcher.group("rate");
            if (rate != null) { queries.put("rate", rate); };
        }

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("speech", file.getName(), RequestBody.create(MediaType.parse(fileMime), file))
                .build();

        String res;
        if (isSingleApiServer) {
            res = postForString(requestBody, queries);
        } else {
            res = postForString("/api/chat", requestBody, queries);
        }
        JSONObject result = new JSONObject(res);
        return result;
    }

    public JSONObject voiceChat(ChatApp chatApp, String uid, String nickname, InputStream inputStream, String codec, String rate) throws IOException {
        return this.voiceChat(chatApp.getAppkey(), chatApp.getAppsecret(), uid, nickname, inputStream, codec, rate);
    }

    public JSONObject voiceChat(String appkey, String appsecret, String uid, String nickname, InputStream inputStream, String codec, String rate) throws IOException {
        HashMap<String, String> queries = generateQuery(appkey, appsecret, uid, nickname);
        queries.put("codec", codec);
        queries.put("rate", rate);

        RequestBody requestBody = RequestBodyUtil.createFromInputStream(OCTECT_STREAM, inputStream);
        String res;
        if (isSingleApiServer) {
            res = postForString(requestBody, queries);
        } else {
            res = postForString("/api/chat", requestBody, queries);
        }
        JSONObject result = new JSONObject(res);
        return result;
    }

    private HashMap<String, String> generateQuery(String appkey, String appsecret, String uid, String nickname) {
        String now = String.valueOf(TimeCalculator.nowInMillis());
        String verify = Md5.digest(appsecret + uid + now);

        HashMap<String, String> queries = new HashMap(5);
        queries.put("appkey", appkey);
        queries.put("uid", uid);
        queries.put("timestamp",now);
        queries.put("verify", verify);
        queries.put("nickname", nickname);

        return queries;
    }

    public JSONObject proxyData(ChatApp chatApp, String uid, JSONObject json) throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = Long.toString(timestamp.getTime());
        String verify = Md5.digest(chatApp.getAppsecret() + uid + time);
        json.put("timestamp", time);
        json.put("uid", uid);
        json.put("verify", verify);

        RequestBody body = RequestBody.create(JSON, json.toString());
        JSONObject res;
        if (isSingleApiServer) {
            res = postForJSON(body);
        } else {
            res = postForJSON("/api/data", body);
        }
        return res;
    }

    public String sendJson(String queryResultType, JSONObject jsonObject, String serverSalt) throws IOException {
        return sendJson(queryResultType, jsonObject, serverSalt, 60);
    }

    public String sendJson(String queryResultType, JSONObject jsonObject, String serverSalt, Integer lifespan) throws IOException {
        String ticket = UUID.randomUUID().toString();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = Long.toString(timestamp.getTime());
        jsonObject.put("search_result_type", queryResultType);

        FormBody body = new FormBody.Builder()
                .add("data", jsonObject.toString())
                .add("key", ticket)
                .add("lifespan", lifespan.toString())
                .add("timestamp", time)
                .add("secret", Md5.digest(time + serverSalt))
                .add("resulttype", queryResultType)
                .build();

        JSONObject resJson;
        if (isSingleApiServer) {
            resJson = postForJSON(body);
        } else {
            resJson = postForJSON("/api/chat/data", body);
        }
        if (resJson != null) {
            return ticket;
        }
        return null;
    }

    public JSONObject getJson(String ticket, String serverSalt) throws IOException {
        HashMap<String, String> request = new HashMap<>(1);
        request.put("key", ticket);

        JSONObject resJson;
        if (isSingleApiServer) {
            resJson = getForJSON(request, null);
        } else {
            resJson = getForJSON("/api/chat/data", request, null);
        }
        if (resJson != null) {
            return resJson;
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

        JSONObject resJson;
        if (isSingleApiServer) {
            resJson = postForJSON(body);
        } else {
            resJson = postForJSON("/api/chat/geo", body);
        }
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

        HashMap<String, String> request = new HashMap<>(3);
        request.put("chatkey", uid);
        request.put("timestamp", time);
        request.put("secret", Md5.digest(time + salt));

        JSONObject resJson;
        if (isSingleApiServer) {
            resJson = getForJSON(request, null);
        } else {
            resJson = getForJSON("/api/chat/geo", request, null);
        }
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

    public String pushData(String appkey, String uid, String msg, String service, String serverSalt) throws IOException {
        return this.pushData(appkey, uid, msg, service, null, serverSalt);
    }

    public String pushData(String appkey, String uid, String msg, String service, JSONObject data, String serverSalt) throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = Long.toString(timestamp.getTime());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", time);
        jsonObject.put("secret", Md5.digest(time + serverSalt));

        jsonObject.put("appkey", appkey);
        jsonObject.put("uid", uid);
        jsonObject.put("msg", msg);
        jsonObject.put("service", service);
        if (data != null) {
            jsonObject.put("data", data);
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        JSONObject resJson = postForJSON("/api/data/push", body);

        if (resJson != null && resJson.has("msg")) {
            return resJson.getString("msg");
        }

        return null;
    }

    @Deprecated
    public String engineChat(String action, Map<String, String> data) throws IOException {
        return (new EngineChatApi(this.getServer())).engineChat(action, data);
    }

    @Deprecated
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
