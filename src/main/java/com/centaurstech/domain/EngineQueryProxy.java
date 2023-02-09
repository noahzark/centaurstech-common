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

    public EngineQueryProxy(Map<String, String> requestParams) {
        super(requestParams);
    }

    public EngineQueryProxy(Map<String, String> requestParams, boolean keepRequestParams) {
        super(requestParams, keepRequestParams);
    }

    /**
     * 新平台用
     *
     * @param uid
     * @param appkey
     * @param chatKey 如果uid和appkey存在，则chatKey备用
     */
    public EngineQueryProxy(String uid, String appkey, String chatKey) {
        super(uid, appkey, chatKey);
    }

    /**
     * 新平台用
     *
     * @param formRequest
     */
    public EngineQueryProxy(FormRequest<?, ?> formRequest) {
        super(formRequest);
    }
}
