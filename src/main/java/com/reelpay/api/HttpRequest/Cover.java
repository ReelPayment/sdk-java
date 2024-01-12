package com.reelpay.api.HttpRequest;

import com.reelpay.api.util.HmacUtil;
import com.reelpay.api.util.Tools;

public class Cover {

    private final Long timestamp;
    private final String appKey;
    private String body;
    private String sign;

    public Cover(String appKey, String body) {
        this.appKey = appKey;
        this.timestamp = Tools.time();
        this.body = body;
    }

    public Cover(String appKey, String body, Long timestamp) {
        this.appKey = appKey;
        this.timestamp = timestamp;
        this.body = body;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean ValidateSign(String signParam) {
        this.hmacSHA256Sign();
        return signParam.equals(this.sign);
    }

    public void hmacSHA256Sign() {
        String hash = this.body + this.timestamp;
        this.sign = HmacUtil.encrypt(hash, this.appKey, HmacUtil.HMAC_SHA256);
    }
}
