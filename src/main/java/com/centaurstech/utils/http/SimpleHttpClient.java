package com.centaurstech.utils.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.centaurstech.utils.QueryHelper.urlEncodeUTF8;

public class SimpleHttpClient {

    private OkHttpClient client;

    String server;

    private Map<String, String> customHeaders
            = new HashMap<>();

    public SimpleHttpClient(String server) {
        this.server = server;
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    Request.Builder buildRequest(Map<String, String> headers, Map<String, String> queries) {
        String queryParameters = "";
        if (queries != null && queries.size() > 0) {
            queryParameters = "?" + queries.entrySet().stream()
                    .map(p -> urlEncodeUTF8(p.getKey()) + "=" + urlEncodeUTF8(p.getValue()))
                    .reduce((p1, p2) -> p1 + "&" + p2)
                    .orElse("");
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(server + queryParameters);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return requestBuilder;
    }

    public String getForString(Map<String, String> headers, Map<String, String> queries) throws IOException {
        if (headers == null)
            headers = customHeaders;
        else
            headers.putAll(customHeaders);
        Request request = buildRequest(headers, queries)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        // System.out.println(res);

        return res;
    }

    public JSONObject getForJSON(Map<String, String> headers, Map<String, String> queries) throws IOException {
        String resStr = getForString(headers, queries);
        try {
            JSONObject resJson = new JSONObject(resStr);
            if(resJson != null && resJson.has("retcode")){
                if(0 == resJson.getInt("retcode")){
                    return resJson;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String postForString(RequestBody body) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("cache-control", "no-cache");
        headers.putAll(customHeaders);

        Request request = buildRequest(headers, null)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        // System.out.println(res);

        return res;
    }

    public JSONObject postForJSON(RequestBody body) throws IOException {
        String resStr = postForString(body);
        try {
            JSONObject resJson = new JSONObject(resStr);
            if(resJson != null && resJson.has("retcode")){
                if(0 == resJson.getInt("retcode")){
                    return resJson;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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

}
