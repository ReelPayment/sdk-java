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

        Map<String, String> header = new HashMap<>();
        header.put("content-type","application/json");
        header.put("X-Timestamp", String.valueOf(cover.getTimestamp()));
        header.put("X-Appid", this.appId);
        header.put("X-Sign", cover.getSign());
        Headers headers = Headers.of(header);
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
