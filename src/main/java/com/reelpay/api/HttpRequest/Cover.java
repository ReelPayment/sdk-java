package com.reelpay.api.HttpRequest;

import com.reelpay.api.util.HmacUtil;

/**
 * Cover class for handling HMAC signature generation and validation
 * 
 * This class is responsible for creating HMAC-SHA256 signatures for API requests
 * and validating signatures for incoming callbacks.
 * 
 * @author ReelPay SDK Team
 * @version 1.0.1
 */
public class Cover {

    /** Application key used for HMAC signature generation */
    private final String appKey;
    
    /** Timestamp for signature generation */
    private final Long timestamp;
    
    /** Request body content */
    private String body;
    
    /** Generated HMAC signature */
    private String signature;

    /**
     * Constructor with current timestamp
     * 
     * @param appKey Application key for HMAC signature
     * @param body Request body content
     */
    public Cover(String appKey, String body) {
        this.appKey = validateAppKey(appKey);
        this.timestamp = System.currentTimeMillis() / 1000L;
        this.body = validateBody(body);
    }

    /**
     * Constructor with custom timestamp
     * 
     * @param appKey Application key for HMAC signature
     * @param body Request body content
     * @param timestamp Custom timestamp for signature generation
     */
    public Cover(String appKey, String body, Long timestamp) {
        this.appKey = validateAppKey(appKey);
        this.timestamp = validateTimestamp(timestamp);
        this.body = validateBody(body);
    }

    /**
     * Get the timestamp used for signature generation
     * 
     * @return timestamp in seconds
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Get the generated HMAC signature
     * 
     * @return HMAC signature string
     */
    public String getSign() {
        return signature;
    }

    /**
     * Get the request body content
     * 
     * @return request body string
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the request body content
     * 
     * @param body new request body content
     */
    public void setBody(String body) {
        this.body = validateBody(body);
    }

    /**
     * Validate a signature against the generated signature
     * 
     * @param signatureToValidate signature to validate
     * @return true if signature matches, false otherwise
     */
    public boolean validateSign(String signatureToValidate) {
        if (signatureToValidate == null || signatureToValidate.isEmpty()) {
            return false;
        }
        
        this.hmacSHA256Sign();
        return signatureToValidate.equals(this.signature);
    }

    /**
     * Generate HMAC-SHA256 signature for the current body and timestamp
     */
    public void hmacSHA256Sign() {
        String hash = this.body + this.timestamp;
        this.signature = HmacUtil.encrypt(hash, this.appKey, HmacUtil.HMAC_SHA256);
    }

    /**
     * Validate application key parameter
     * 
     * @param appKey application key to validate
     * @return validated application key
     * @throws IllegalArgumentException if appKey is null or empty
     */
    private String validateAppKey(String appKey) {
        if (appKey == null || appKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Application key cannot be null or empty");
        }
        return appKey;
    }

    /**
     * Validate timestamp parameter
     * 
     * @param timestamp timestamp to validate
     * @return validated timestamp
     * @throws IllegalArgumentException if timestamp is null or invalid
     */
    private Long validateTimestamp(Long timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        if (timestamp <= 0) {
            throw new IllegalArgumentException("Timestamp must be positive");
        }
        return timestamp;
    }

    /**
     * Validate body parameter
     * 
     * @param body body content to validate
     * @return validated body content
     * @throws IllegalArgumentException if body is null
     */
    private String validateBody(String body) {
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
        return body;
    }
}
