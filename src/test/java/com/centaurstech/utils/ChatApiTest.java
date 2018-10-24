package com.centaurstech.utils;

import com.centaurstech.domain.ChatApp;
import com.centaurstech.domain.EngineQuery;
import com.centaurstech.domain.EngineQueryProxy;
import com.centaurstech.domain.GPSLocation;
import org.hamcrest.core.IsNull;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class ChatApiTest {

    @Test
    public void testChatApi() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/chat");

        ChatApp chatApp = new ChatApp("qiwurobot", "123456");
        String nickname = "common-lib-test";

        String uid = GetNetworkAddress.GetAddress("mac");

        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/chat");
//        JSONObject result = chatApi.chat(appkey, appsecret, uid, nickname,
//                "HELLO");
        JSONObject result = chatApi.chat(chatApp, uid, nickname,"HELLO");

        assertThat(result.get("retcode"), is(0));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testSendJson() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/chat/data");

        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/chat/data");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");

        String SERVER_SALT = " QUERY_RESULT_SALT";
        String ticket = chatApi.sendJson("test_result_type", jsonObject, SERVER_SALT, 600);
        assertThat(ticket, is(IsNull.notNullValue()));
        System.out.println(ticket);

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testGetJson() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/chat/data");

        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/chat/data");
        String SERVER_SALT = " QUERY_RESULT_SALT";
        String ticket = "e5cf5e19-5495-491e-b69f-6c53cfc216bc";
        JSONObject result = chatApi.getJson(ticket, SERVER_SALT);
        assertThat(result, is(IsNull.notNullValue()));
        System.out.println(result.toString());

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testSendGPS() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("POST /api/chat/geo");

        GPSLocation location = new GPSLocation(1.2d, 2.1d);

        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/chat/geo");

        String msg = chatApi.sendGPS("qiwurobot", "123456", "common-lib-test", location);
        assertThat(msg, is("added"));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testGetGPS() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("GET /api/chat/geo");

        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/chat/geo");

        GPSLocation location = chatApi.getGPS("common-lib-test", " XXXXXX");
        assertThat(location, is(IsNull.notNullValue()));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testEngineLogin() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/goingchatcn");

        ChatApi chatApi = new ChatApi("http://qcloud-sh1.chewrobot.com/goingchatcn/chatbotserver.php");
        HashMap<String, String> loginRequest = new HashMap<>();
        loginRequest.put("aipioneer_username", "mimi2");
        loginRequest.put("nickname", "landey");
        JSONObject result = chatApi.engineChatJson("start", loginRequest);

        assertThat(result, is(IsNull.notNullValue()));
        assertThat(result.has("chat_key"), is(true));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testSettingHeaders() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/addHeaders");

        ChatApi chatApi = new ChatApi("http://echo.chewrobot.com/anything");
        chatApi.addCustomHeader("X-Forwarded-For", "121.35.103.1");
        chatApi.addCustomHeader("Qw-Connecting-Ip", "121.35.103.1");

        String result = chatApi.getForString(null, null);

        System.out.println(result);
        System.out.println(engineQuery.getQueryTimeString());
    }

}
