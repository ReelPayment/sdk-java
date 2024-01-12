package com.reelpay.api.HttpRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private static final OkHttpClient client = new OkHttpClient();
    protected String appKey;
    protected String appId;
    protected String baseUrl;

    protected JsonObject request(String url, JsonObject jsonBody)
    {
        Cover cover = new Cover(this.appKey, jsonBody.toString());
        cover.hmacSHA256Sign();

        Map<String, String> hd = new HashMap<>();
        hd.put("content-type","application/json");
        hd.put("X-Timestamp", String.valueOf(cover.getTimestamp()));
        hd.put("X-Appid", this.appId);
        hd.put("X-Sign", cover.getSign());
        Headers headers = Headers.of(hd);
        RequestBody body = RequestBody.create(cover.getBody().getBytes(StandardCharsets.UTF_8));
        Request request = new Request.Builder()
                .url(this.baseUrl + url)
                .headers(headers)
                .post(body)
                .build();

        JsonObject rt = new JsonObject();
        try (Response response = client.newCall(request).execute()) {
            String result = null;
            if (response.body() != null) {
                result = response.body().string();
                rt = JsonParser.parseString(result).getAsJsonObject();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rt;
    }
}
