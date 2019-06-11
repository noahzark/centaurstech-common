package com.centaurstech.utils;

import com.centaurstech.domain.ChatApp;
import com.centaurstech.domain.EngineQuery;
import com.centaurstech.domain.EngineQueryProxy;
import com.centaurstech.domain.GPSLocation;
import org.hamcrest.core.IsNull;
import org.json.JSONObject;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class ChatApiTest {

    @Test
    public void testChatApi() throws Exception {
        String uid = GetNetworkAddress.GetAddress("mac");
        JSONObject result = this.testChatApi(uid, "HELLO");

        assertThat(result.get("retcode"), is(0));
    }

    static JSONObject testChatApi(String uid, String ask) throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/chat");

        String appkey = "qiwurobot";
        String appsecret = "123456";
        String nickname = "common-lib-test";
        ChatApp chatApp = new ChatApp(appkey, appsecret);

        ChatApi chatApi = new ChatApi("http://localhost:18001/api/chat");

        /* Chat without chatApp
        JSONObject result = chatApi.chat(appkey, appsecret, uid, nickname,
                "HELLO");
        */
        JSONObject result = chatApi.chat(chatApp, uid, nickname,ask);

        System.out.println(engineQuery.getQueryTimeString());
        return result;
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

        ChatApi chatApi = new ChatApi("http://robot-engine.centaurstech.com/goingchatcn/chatbotserver.php");
        HashMap<String, String> loginRequest = new HashMap<>();
        loginRequest.put("aipioneer_username", "mimi2");
        loginRequest.put("nickname", "landey");
        JSONObject result = chatApi.engineChatJson("start", loginRequest);

        assertThat(result, is(IsNull.notNullValue()));
        assertThat(result.has("chat_key"), is(true));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testEngineChat() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/goingchatcn");

        // init request
        ChatApi chatApi = new ChatApi("http://robot-engine.centaurstech.com/goingchatcn/chatbotserver.php");
        HashMap<String, String> req = new HashMap<>();
        req.put("aipioneer_username", "mimi2");
        req.put("nickname", "landey");

        // login
        JSONObject result = chatApi.engineChatJson("start", req);
        String chatKey = result.getString("chat_key");
        req.put("chat_key", chatKey);
        String answer = result.optString("reply", "");

        // wait for welcome
        while (answer.isEmpty()) {
            Thread.sleep(1000); System.out.println("-1s ");
            answer = chatApi
                    .engineChatJson("receive", req)
                    .optString("reply", "");
        }
        System.out.println("Welcome: " + answer);

        // send message
        HashMap<String, String> chatReq = new HashMap<>();
        chatReq.putAll(req);
        // ask robot
        chatReq.put("message", "1");
        String temp = chatApi
                .engineChat("send", chatReq);
        answer = new JSONObject(temp)
                .optString("reply", "");
        System.out.println("Sent: " + chatReq.get("message") + " Got: " + temp);

        // wait for answer
        while (answer.isEmpty()) {
            Thread.sleep(1000); System.out.println("-1s ");
            answer = chatApi
                    .engineChatJson("receive", req)
                    .optString("reply", "");
        }
        System.out.println("answer: " + answer);

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

class ChatApiTerminal {

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String uid = GetNetworkAddress.GetAddress("mac");

        System.out.println("Loading...");
        System.out.println(ChatApiTest.testChatApi(uid, "HELLO").get("msg"));

        String line;
        while(!(line = bufferedReader.readLine()).equals("exit")) {
            JSONObject result = ChatApiTest.testChatApi(uid, line);
            System.out.println(result.get("msg"));
            result.remove("msg");
            System.out.println(result);
        }

        System.out.println("Bye");
    }

}
