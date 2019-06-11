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

/**
 * @author feli
 */
public class SimpleHttpClient extends BaseHttpClient {

    public SimpleHttpClient(String server, long readTimeout) {
        super(server, readTimeout);
    }

    public SimpleHttpClient(String server) {
        super(server, 30);
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
        if (headers == null) {
            headers = customHeaders;
        } else {
            headers.putAll(customHeaders);
        }
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
        return getJsonObjectFromResponseString(resStr);
    }

    public String postForString(RequestBody body) throws IOException {
        return postForString(body, null);
    }

    public String postForString(RequestBody body, Map<String, String> queries) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("cache-control", "no-cache");
        headers.putAll(customHeaders);

        Request request = buildRequest(headers, queries)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        // System.out.println(res);

        return res;
    }

    public JSONObject postForJSON(RequestBody body) throws IOException {
        return postForJSON(body, null);
    }

    public JSONObject postForJSON(RequestBody body, Map<String, String> queries) throws IOException {
        String resStr = postForString(body, queries);
        return getJsonObjectFromResponseString(resStr);
    }

    private JSONObject getJsonObjectFromResponseString(String resStr) {
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

}
