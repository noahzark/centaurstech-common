package com.centaurstech.utils.http;

import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseHttpClient {

    String server;

    Map<String, String> customHeaders;

    OkHttpClient client;

    public BaseHttpClient(String server, long readTimeout) {
        this.server = server;
        customHeaders = new HashMap<>();
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
    }

    public BaseHttpClient(String server) {
        this(server, 30);
    }

    /**
     * Add a customized request header to all requests sent using chat api
     * @param key
     * @param value
     * @return
     */
    public String addCustomHeader(String key, String value) {
        return customHeaders.put(key, value);
    }

    /**
     * Remove customized request header in chat api
     * @param key
     * @return
     */
    public String removeCustomHeader(String key) {
        return customHeaders.remove(key);
    }

    public String getServer() {
        return server;
    }
}
