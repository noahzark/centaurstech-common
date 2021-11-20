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
    //请求对话接口测试，post方法
    public void testChatApi() throws Exception {
        String uid = GetNetworkAddress.GetAddress("mac");
        JSONObject result = this.testChatApi(uid, "你好啊");

        assertThat(result.get("retcode"), is(0));
    }

    static JSONObject testChatApi(String uid, String ask) throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/chat");

        String appkey = "qiwurobot";
        String appsecret = "123456";
        String nickname = "common-lib-test";
        ChatApp chatApp = new ChatApp(appkey, appsecret);

        ChatApi chatApi = new ChatApi("http://localhost:18001");

        /* Chat without chatApp
        JSONObject result = chatApi.chat(appkey, appsecret, uid, nickname,
                "HELLO");
        */
        JSONObject result = chatApi.chat(chatApp, "15619811", nickname,ask);

        System.out.println(engineQuery.getQueryTimeString());
        return result;
    }

    @Test
    public void testProxyData() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/chat/data");
        ChatApi chatApi = new ChatApi("http://localhost:18001");
        JSONObject jsonObject = new JSONObject("{\"extra_order_info\":{\"skillName\":\"RECHARGE\",\"scene\":\"RECHARGE_MEMORY_UPDATE\",\"data\":{\"clear_query\":false,\"erased_fields\":\"\",\"set_fields\":{\"phone_num\":\"123456\",\"money\":\"100\",\"name\":\"\"}}},\"appkey\":\"qiwurobot\",\"lang\":\"zh\",\"timestamp\":\"1615287470\"}");

        String appkey = "qiwurobot";
        String appsecret = "123456";
        ChatApp chatApp = new ChatApp(appkey, appsecret);
        JSONObject result = chatApi.proxyData(chatApp, "123456", jsonObject);
        assertThat(result, is(IsNull.notNullValue()));
        System.out.println(result.toString());

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    //测试向对话接口发送Json数据
    public void testSendJson() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/chat/data");

        ChatApi chatApi = new ChatApi("http://localhost:18001");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");

        String SERVER_SALT = " QUERY_RESULT_SALT";
        String ticket = chatApi.sendJson("test_result_type", jsonObject, SERVER_SALT, 600);
        assertThat(ticket, is(IsNull.notNullValue()));
        System.out.println(ticket);

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    //测试向对话接口发送Push数据
    public void testPushData() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/data/push");

        ChatApi chatApi = new ChatApi("http://localhost:18001");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");

        String SERVER_SALT = " QUERY_RESULT_SALT";
        String message = chatApi.pushData("qiwurobot", "123456","HELLO","TestService", SERVER_SALT);
        assertThat(message, is("success"));
        System.out.println(message);

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    //测试请求对话接口后，并将接口返回的数据变成Json的格式
    public void testGetJson() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/api/chat/data");

        ChatApi chatApi = new ChatApi("http://localhost:18001");
        String SERVER_SALT = " QUERY_RESULT_SALT";
        String ticket = "e5cf5e19-5495-491e-b69f-6c53cfc216bc";
        JSONObject result = chatApi.getJson(ticket, SERVER_SALT);
        assertThat(result, is(IsNull.notNullValue()));
        System.out.println(result.toString());

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    //向定位接口发送GPS定位信息，并测试返回的msg
    public void testSendGPS() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("POST /api/chat/geo");

        GPSLocation location = new GPSLocation(1.2d, 2.1d);

        ChatApi chatApi = new ChatApi("http://localhost:18001");

        String msg = chatApi.sendGPS("qiwurobot", "123456", "common-lib-test", location);
        assertThat(msg, is("added"));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    //测试请求定位接口后，获取接口返回的定位信息
    public void testGetGPS() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("GET /api/chat/geo");

        ChatApi chatApi = new ChatApi("http://localhost:18001");

        GPSLocation location = chatApi.getGPS("common-lib-test", " GET_GEO_LOCATION_SALT");
        assertThat(location, is(IsNull.notNullValue()));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    //测试登陆接口
    public void testEngineLogin() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/goingchatcn");

        EngineChatApi chatApi = new EngineChatApi("http://aliyun-sh6.chewrobot.com/goingchatcn/chatbotserver.php");
        HashMap<String, String> loginRequest = new HashMap<>();
        loginRequest.put("aipioneer_username", "mimi2");
        loginRequest.put("nickname", "landey");
        JSONObject result = chatApi.engineChatJson("start", loginRequest);

        assertThat(result, is(IsNull.notNullValue()));
        assertThat(result.has("chat_key"), is(true));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    //测试登陆对话整个流程
    public void testEngineChat() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/goingchatcn");

        // init request
        EngineChatApi chatApi = new EngineChatApi("http://aliyun-sh10.chewrobot.com/goingchatcn/chatbotserver.php");
        HashMap<String, String> req = new HashMap<>();
        req.put("aipioneer_username", "HotelTest");
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
        chatReq.put("message", "我要订深圳的酒店。");
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
    //重置请求头信息
    public void testSettingHeaders() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/addHeaders");

        ChatApi chatApi = new ChatApi("http://echo.chewrobot.com");
        chatApi.addCustomHeader("X-Forwarded-For", "121.35.103.1");
        chatApi.addCustomHeader("Qw-Connecting-Ip", "121.35.103.1");

        String result = chatApi.getForString("/anything", null, null);

        System.out.println(result);
        System.out.println(engineQuery.getQueryTimeString());
    }

}
