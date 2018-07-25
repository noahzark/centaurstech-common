package com.centaurstech.domain;

import java.util.Map;

/**
 * Proxy class for engine query
 * Created by Feliciano on 7/3/2018.
 */
public class EngineQueryProxy extends EngineQuery {

    public EngineQueryProxy(String chat_key) {
        super(chat_key);
    }

    public EngineQueryProxy(Map<String,String> requestParams) {
        super(requestParams);
    }

}
