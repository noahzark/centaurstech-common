package com.centaurstech.domain;

import org.hamcrest.core.IsNull;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class EngineQueryTest {

    @Test
    //对EngineQueryProxy各构造方法、get、hasValue的测试
    public void testEngineQuery() {
        HashMap<String, String> requestParam = new HashMap();

        requestParam.put("chat_key", "123456@@@987654");
        EngineQuery engineQuery = new EngineQueryProxy(requestParam);
        assertThat(engineQuery.getChatKey(), is("123456"));

        engineQuery = new EngineQueryProxy("123456@@@987654");
        assertThat(engineQuery.getChatKey(), is("123456"));

        requestParam.put("chat_key", "123456###987654");
        engineQuery = new EngineQueryProxy(requestParam);
        assertThat(engineQuery.getChatKey(), is("123456###987654"));

        requestParam.put("chat_key", "123456@@@");
        engineQuery = new EngineQueryProxy(requestParam);
        assertThat(engineQuery.getChatKey(), is("123456"));

        requestParam.put("chat_key", "123@@@456###987654");
        engineQuery = new EngineQueryProxy(requestParam);
        assertThat(engineQuery.getChatKey(), is("123"));
        assertThat(engineQuery.getExtra(), is("456"));

        requestParam.put("chat_key", "123@@@456###");
        engineQuery = new EngineQueryProxy(requestParam, true);
        assertThat(engineQuery.getChatKey(), is("123"));
        assertThat(engineQuery.getExtra(), is("456"));

        requestParam.put("hi", "true");
        assertThat(engineQuery.getStringValue("hi"), is("true"));
        assertThat(engineQuery.hasValue("hi"), is(true));

        requestParam.put("hello", "123");
        assertThat(engineQuery.getIntegerValue("hello"), is(123));
    }

}
