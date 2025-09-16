package com.reelpay.api.HttpRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.reelpay.api.constants.ReelPayConstants;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Base HTTP client for ReelPay API requests
 * 
 * This class handles HTTP communication with ReelPay API endpoints,
 * including signature generation, request building, and response handling.
 * 
 * @author ReelPay SDK Team
 * @version 1.0.1
 */
public class Client {
    
    /** HTTP client instance with optimized configuration */
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
    
    /** Logger instance for this class */
    private static final Logger logger = LogManager.getLogger(Client.class);
    
    /** Application key for API authentication */
    protected String appKey;
    
    /** Application ID for API authentication */
    protected String appId;
    
    /** Base URL for API endpoints */
    protected String baseUrl;

    /**
     * Make an authenticated HTTP request to the ReelPay API
     * 
     * @param endpoint API endpoint path (e.g., "/v1/transactions/pay")
     * @param requestBody JSON request body
     * @return JsonObject API response
     * @throws RuntimeException if request fails or response is invalid
     */
    protected JsonObject request(String endpoint, JsonObject requestBody) {
        validateRequestParameters(endpoint, requestBody);
        
        try {
            // Generate HMAC signature
            Cover cover = new Cover(this.appKey, requestBody.toString());
            cover.hmacSHA256Sign();

            // Build request headers
            Map<String, String> headers = buildRequestHeaders(cover);
            
            // Create HTTP request
            Request httpRequest = buildHttpRequest(endpoint, headers, cover.getBody());
            
            // Execute request and handle response
            return executeRequest(httpRequest);
            
        } catch (Exception e) {
            logger.error("Request failed for endpoint {}: {}", endpoint, e.getMessage(), e);
            throw new RuntimeException("API request failed: " + e.getMessage(), e);
        }
    }

    /**
     * Validate request parameters
     * 
     * @param endpoint API endpoint
     * @param requestBody JSON request body
     * @throws IllegalArgumentException if parameters are invalid
     */
    private void validateRequestParameters(String endpoint, JsonObject requestBody) {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            throw new IllegalArgumentException("Endpoint cannot be null or empty");
        }
        if (requestBody == null) {
            throw new IllegalArgumentException("Request body cannot be null");
        }
        if (this.appKey == null || this.appKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Application key is not set");
        }
        if (this.appId == null || this.appId.trim().isEmpty()) {
            throw new IllegalArgumentException("Application ID is not set");
        }
        if (this.baseUrl == null || this.baseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Base URL is not set");
        }
    }

    /**
     * Build request headers for API authentication
     * 
     * @param cover Cover instance with signature information
     * @return Map of request headers
     */
    private Map<String, String> buildRequestHeaders(Cover cover) {
        Map<String, String> headers = new HashMap<>();
        headers.put(ReelPayConstants.HEADER_CONTENT_TYPE, ReelPayConstants.CONTENT_TYPE_JSON);
        headers.put(ReelPayConstants.HEADER_TIMESTAMP, String.valueOf(cover.getTimestamp()));
        headers.put(ReelPayConstants.HEADER_APP_ID, this.appId);
        headers.put(ReelPayConstants.HEADER_SIGNATURE, cover.getSign());
        headers.put(ReelPayConstants.HEADER_USER_AGENT, ReelPayConstants.USER_AGENT);
        return headers;
    }

    /**
     * Build HTTP request object
     * 
     * @param endpoint API endpoint
     * @param headers Request headers
     * @param body Request body
     * @return OkHttp Request object
     */
    private Request buildHttpRequest(String endpoint, Map<String, String> headers, String body) {
        Headers okHttpHeaders = Headers.of(headers);
        RequestBody requestBody = RequestBody.create(body.getBytes(StandardCharsets.UTF_8));
        
        return new Request.Builder()
                .url(this.baseUrl + endpoint)
                .headers(okHttpHeaders)
                .post(requestBody)
                .build();
    }

    /**
     * Execute HTTP request and handle response
     * 
     * @param request HTTP request to execute
     * @return JsonObject API response
     * @throws IOException if request execution fails
     * @throws JsonSyntaxException if response parsing fails
     */
    private JsonObject executeRequest(Request request) throws IOException, JsonSyntaxException {
        logger.debug("Making request to: {}", request.url());
        
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            int statusCode = response.code();
            logger.debug("Response status code: {}", statusCode);
            
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                logger.error("HTTP request failed with status {}: {}", statusCode, errorBody);
                throw new IOException("HTTP request failed with status " + statusCode + ": " + errorBody);
            }
            
            if (response.body() == null) {
                logger.warn("Response body is null");
                return new JsonObject();
            }
            
            String responseBody = response.body().string();
            logger.debug("Response body: {}", responseBody);
            
            try {
                return JsonParser.parseString(responseBody).getAsJsonObject();
            } catch (JsonSyntaxException e) {
                logger.error("Failed to parse JSON response: {}", responseBody, e);
                throw new JsonSyntaxException("Invalid JSON response: " + responseBody, e);
            }
        }
    }
}
