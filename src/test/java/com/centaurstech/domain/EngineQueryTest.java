package com.centaurstech.domain;

import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class EngineQueryTest {

    @Test
    public void testEngineQuery() {
        HashMap<String, String> requestParam = new HashMap();
        requestParam.put("chat_key", "123456@@@987654");
        EngineQuery engineQuery = new EngineQueryProxy(requestParam);

        assertThat(engineQuery.getChatKey(), is("123456"));
    }

}
