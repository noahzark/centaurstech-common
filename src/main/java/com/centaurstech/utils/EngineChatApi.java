package com.centaurstech.utils;

import com.centaurstech.utils.http.SimpleHttpClient;
import okhttp3.FormBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * @author Fangzhou.Long on 9/19/2019
 * @project common
 */
public class EngineChatApi extends SimpleHttpClient {

    EngineChatApi(String server) {
        super(server);
    }

    public String engineChat(String action, Map<String, String> data) throws IOException {
        FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("action", action)
                .add("format", "json");
        for (String entry : data.keySet()) {
            bodyBuilder.add(entry, data.get(entry));
        }
        FormBody body = bodyBuilder.build();

        String resStr;
        if (isSingleApiServer) {
            resStr = postForString(body);
        } else {
            resStr = postForString("/goingchatcn/chatbotserver.php", body);
        }
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
