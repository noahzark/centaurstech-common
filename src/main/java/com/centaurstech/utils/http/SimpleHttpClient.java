package com.centaurstech.utils.http;

import com.centaurstech.utils.StringExtractor;
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

    public boolean isSingleApiServer;

    public SimpleHttpClient(String server, long readTimeout) {
        super(server, readTimeout);
        isSingleApiServer = StringExtractor.countOccurences(this.server, '/') > 2;
    }

    public SimpleHttpClient(String server) {
        this(server, 30);
    }

    @Deprecated
    Request.Builder buildRequest(Map<String, String> headers, Map<String, String> queries) {
        return buildRequest("", headers, queries);
    }

    Request.Builder buildRequest(String api, Map<String, String> headers, Map<String, String> queries) {
        String queryParameters = "";
        if (queries != null && queries.size() > 0) {
            queryParameters = "?" + queries.entrySet().stream()
                    .map(p -> urlEncodeUTF8(p.getKey()) + "=" + urlEncodeUTF8(p.getValue()))
                    .reduce((p1, p2) -> p1 + "&" + p2)
                    .orElse("");
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(server + api + queryParameters);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return requestBuilder;
    }

    @Deprecated
    public String getForString(Map<String, String> headers, Map<String, String> queries) throws IOException {
        return getForString("", headers, queries);
    }

    public String getForString(String api, Map<String, String> headers, Map<String, String> queries) throws IOException {
        if (headers == null) {
            headers = customHeaders;
        } else {
            headers.putAll(customHeaders);
        }
        Request request = buildRequest(api, headers, queries)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        // System.out.println(res);

        return res;
    }

    @Deprecated
    public JSONObject getForJSON(Map<String, String> headers, Map<String, String> queries) throws IOException {
        return getForJSON("", headers, queries);
    }

    public JSONObject getForJSON(String api, Map<String, String> headers, Map<String, String> queries) throws IOException {
        String resStr = getForString(api, headers, queries);
        return getJsonObjectFromResponseString(resStr);
    }

    @Deprecated
    public String postForString(RequestBody body) throws IOException {
        return postForString("", body);
    }

    public String postForString(String api, RequestBody body) throws IOException {
        return postForString(api, body, null);
    }

    @Deprecated
    public String postForString(RequestBody body, Map<String, String> queries) throws IOException {
        return postForString("", body, queries);
    }

    @Deprecated
    public String postForString(RequestBody body, Map<String, String> headers, Map<String, String> queries) throws IOException {
        return postForString("", headers, body, queries);
    }

    public String postForString(String api, RequestBody body, Map<String, String> queries) throws IOException {
        return postForString(api, new HashMap<>(), body, queries);
    }

    public String postForString(String api, Map<String, String> headers, RequestBody body, Map<String, String> queries) throws IOException {
        headers.putAll(customHeaders);

        Request request = buildRequest(api, headers, queries)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String res = null;
        if (response.body() != null) {
            res = response.body().string();
        }

        return res;
    }

    @Deprecated
    public JSONObject postForJSON(RequestBody body) throws IOException {
        return postForJSON("", body);
    }

    public JSONObject postForJSON(String api, RequestBody body) throws IOException {
        return postForJSON(api, body, null);
    }

    @Deprecated
    public JSONObject postForJSON(RequestBody body, Map<String, String> queries) throws IOException {
        return postForJSON("", body, queries);
    }

    public JSONObject postForJSON(String api, RequestBody body, Map<String, String> queries) throws IOException {
        String resStr = postForString(api, body, queries);
        return getJsonObjectFromResponseString(resStr);
    }

    public JSONObject postForJSON(String api, Map<String, String> headers, RequestBody body, Map<String, String> queries) throws IOException {
        String resStr = postForString(api, headers, body, queries);
        return getJsonObjectFromResponseString(resStr);
    }

    private static JSONObject getJsonObjectFromResponseString(String resStr) {
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
