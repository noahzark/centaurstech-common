package com.centaurstech.utils;

import org.hamcrest.core.IsNull;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class ChatApiTest {

    @Test
    public void testChatApi() throws Exception {
        String appkey = "match-cps";
        String appsecret = "8060ee6ba5ea4bdeb0040d0317fd2cbf";
        String nickname = "common-lib-test";

        String uid = GetNetworkAddress.GetAddress("mac");

        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/chat");
        JSONObject result = chatApi.chat(appkey, appsecret, uid, nickname,
                "HELLO");
        assertThat(result.get("retcode"), is(0));
    }

    @Test
    public void testSendJson() throws Exception {
        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/chat/data");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");

        String SERVER_SALT = " QUERY_RESULT_SALT";
        String ticket = chatApi.sendJson("test_result_type", jsonObject, SERVER_SALT);
        assertThat(ticket, is(IsNull.notNullValue()));
    }

}
