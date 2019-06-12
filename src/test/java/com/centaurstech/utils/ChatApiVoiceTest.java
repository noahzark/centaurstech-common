package com.centaurstech.utils;

import com.centaurstech.domain.ChatApp;
import com.centaurstech.domain.EngineQuery;
import com.centaurstech.domain.EngineQueryProxy;
import org.hamcrest.core.IsNull;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ChatApiVoiceTest {

    String uid = GetNetworkAddress.GetAddress("mac");
    String appkey = "qiwurobot";
    String appsecret = "123456";
    String nickname = "common-lib-test";
    ChatApp chatApp = new ChatApp(appkey, appsecret);

    @Test
    public void testSendVoiceFileInForm() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/inForm");

        ChatApp chatApp = new ChatApp(appkey, appsecret);
        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/speech/chat");
        JSONObject result = chatApi.voiceChat(
                chatApp, uid, nickname,
                new File("./example/test.amr"), "audio/amr;codec=amr;rate=16000;channel=1");
        System.out.println(result.opt("answer"));
        result.remove("answer");
        System.out.println(result);
        assertThat(result, is(IsNull.notNullValue()));
        assertThat(result.has("ask"), is(true));

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testSendVoiceFileInStream() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("/inStream");

        ChatApi chatApi = new ChatApi("https://robot-service.centaurstech.com/api/speech/chat");
        JSONObject result = chatApi.voiceChat(
                chatApp, uid, nickname,
                new FileInputStream(new File("./example/test.pcm")),
                "pcm", "8000");
        System.out.println(result.opt("answer"));
        result.remove("answer");
        System.out.println(result);
        assertThat(result, is(IsNull.notNullValue()));
        assertThat(result.has("ask"), is(true));

        System.out.println(engineQuery.getQueryTimeString());
    }

}
