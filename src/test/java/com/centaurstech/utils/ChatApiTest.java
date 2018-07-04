package com.centaurstech.utils;

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
        String appkey = "shenghan";
        String appsecret = "4bc6f0b4fa29445a6fe0ae3ca471bdd3";
        String nickname = "common-lib-test";

        String uid = GetNetworkAddress.GetAddress("mac");

        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/chat");
        JSONObject result = chatApi.chat(appkey, appsecret, uid, nickname,
                "HELLO");
        assertThat(result.get("retcode"), is(0));
    }
}
