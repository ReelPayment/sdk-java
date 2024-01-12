package com.reelpay.api;

import com.google.gson.JsonObject;
import com.reelpay.api.HttpRequest.Client;
import com.reelpay.api.HttpRequest.Cover;
import com.reelpay.api.util.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Transactions extends Client {
    private static final String API_URL = "https://api.reelpay.com";
    private final String[] STATUS = {"PAID","TIME-OUT","CLOSE","REFUND-IN-PROGRESS","REFUNDED","ABNORMAL","CHAIN-CONFIRMATION-FAILED"};
    private final Logger log = LogManager.getLogger(this.getClass());

    public Transactions(String appId, String appKey) {
        super.baseUrl = API_URL;
        super.appId = appId;
        super.appKey = appKey;
    }

    public JsonObject Pay(JsonObject req) {
        return request("/v1/transactions/pay", req);
    }

    public JsonObject Amount(JsonObject req) {
        return request("/v1/transactions/amount", req);
    }

    public JsonObject Transaction(JsonObject req) {
        return request("/v1/transactions", req);
    }

    public JsonObject Close(JsonObject req) {
        return request("/v1/transactions/close", req);
    }

    public JsonObject Currency(JsonObject req) {
        return request("/v1/transactions/currency", req);
    }

    public JsonObject Refund(JsonObject req) {
        return request("/v1/transactions/refund", req);
    }

    /**
     * EntrustPay
     * @param out_trade_no out_trade_no
     * @param symbol Legal currency unit
     * @param amount Product price, like 2.00|0.50, Keep 2 decimal places
     * @param name Product name
     * @param image Product image
     * @return JsonObject
     */
    public JsonObject EntrustPay(String out_trade_no, String symbol, String amount, String name, String image) {
        JsonObject req = new JsonObject();
        req.addProperty("out_trade_no", out_trade_no);
        req.addProperty("symbol", symbol);
        req.addProperty("amount", amount);
        req.addProperty("name", name);
        req.addProperty("image", image);
        return request("/v1/transactions/entrust", req);
    }

    /**
     * VerifyCallbackSignature, Please remember to verify the order and amount
     * @param body reqBody
     * @param header reqHeader
     * @return boolean
     */
    public Boolean VerifyCallbackSignature(String body, HashMap<String, String> header){
        try {
            String appid = header.get("X-Appid");
            String Timestamp = header.get("X-Timestamp");
            String Sign = header.get("X-Sign");
            String EventType = header.get("X-EventType");
            if(!this.appId.equals(appid)){
                log.error("appid error");
                return false;
            }
            if(!Tools.in_array(EventType, STATUS)){
                log.error("EventType error");
                return false;
            }
            //body + Timestamp
            Cover cover = new Cover(this.appKey, body, Long.valueOf(Timestamp));
            if(!cover.ValidateSign(Sign)){
                log.error("ValidateSign error");
                return false;
            }
            return true;
        } catch (Throwable e) {
            log.error("parameter error: " + e.getMessage());
            return false;
        }
    }
}
