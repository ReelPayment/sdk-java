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

    /**
     * Create Payment Order
     * @param req JsonObject
     * @return JsonObject
     */
    public JsonObject Pay(JsonObject req) {
        return request("/v1/transactions/pay", req);
    }

    /**
     * Create Payment Order
     * @param out_trade_no Merchant Order ID
     * @param currency_id Currency ID
     * @param fiat_name Fiat name
     * @param fiat_amount Fiat amount
     * @return JsonObject
     */
    public JsonObject Pay(String out_trade_no, String currency_id, String fiat_name, String fiat_amount) {
        JsonObject req = new JsonObject();
        req.addProperty("out_trade_no", out_trade_no);
        req.addProperty("currency_id", currency_id);
        req.addProperty("fiat_name", fiat_name);
        req.addProperty("fiat_amount", fiat_amount);
        return request("/v1/transactions/pay", req);
    }

    /**
     * Create Payment Order
     * @param out_trade_no Merchant Order ID
     * @param currency_id Currency ID
     * @param fiat_name Fiat name
     * @param fiat_amount Fiat amount
     * @param callback callback URL
     * @return JsonObject
     */
    public JsonObject Pay(String out_trade_no, String currency_id, String fiat_name, String fiat_amount, String callback) {
        JsonObject req = new JsonObject();
        req.addProperty("out_trade_no", out_trade_no);
        req.addProperty("currency_id", currency_id);
        req.addProperty("fiat_name", fiat_name);
        req.addProperty("fiat_amount", fiat_amount);
        req.addProperty("callback", callback);
        return request("/v1/transactions/pay", req);
    }

    /**
     * Get Amount (Get the current exchange price between TOKEN and fiat currency.)
     * @param req JsonObject
     * @return JsonObject
     */
    public JsonObject Amount(JsonObject req) {
        return request("/v1/transactions/amount", req);
    }

    /**
     * Get Amount (Get the current exchange price between TOKEN and fiat currency.)
     * @param currency_id Currency ID (https://docs.reelpay.com/payment-api/api-interface#token-list)
     * @param fiat_name Fiat name (USD)
     * @param fiat_amount Fiat amount (1.2)
     * @return JsonObject
     */
    public JsonObject Amount(String currency_id, String fiat_name, String fiat_amount) {
        JsonObject req = new JsonObject();
        req.addProperty("currency_id", currency_id);
        req.addProperty("fiat_name", fiat_name);
        req.addProperty("fiat_amount", fiat_amount);
        return request("/v1/transactions/amount", req);
    }

    /**
     * Order Information Interface
     * @param req JsonObject
     * @return JsonObject
     */
    public JsonObject Transaction(JsonObject req) {
        return request("/v1/transactions", req);
    }

    /**
     * Order Information Interface
     * @param trade_no ReelPay order ID
     * @return JsonObject
     */
    public JsonObject Transaction(String trade_no) {
        JsonObject req = new JsonObject();
        req.addProperty("trade_no", trade_no);
        return request("/v1/transactions", req);
    }


    /**
     * Close Order
     * @param req JsonObject
     * @return JsonObject
     */
    public JsonObject Close(JsonObject req) {
        return request("/v1/transactions/close", req);
    }

    /**
     * Close Order
     * @param trade_no ReelPay order ID
     * @return JsonObject
     */
    public JsonObject Close(String trade_no) {
        JsonObject req = new JsonObject();
        req.addProperty("trade_no", trade_no);
        return request("/v1/transactions/close", req);
    }

    public JsonObject Currency() {
        return request("/v1/transactions/currency", new JsonObject());
    }

    public JsonObject Refund(JsonObject req) {
        return request("/v1/transactions/refund", req);
    }

    /**
     * Request Refund
     * @param txid TXID transaction on the chain
     * @param trade_no ReelPay order ID
     * @param fuel GAS fee deduction method「1: merchant 2: user」
     * @return JsonObject
     */
    public JsonObject Refund(String txid, String trade_no, int fuel) {
        JsonObject req = new JsonObject();
        req.addProperty("txid", txid);
        req.addProperty("trade_no", trade_no);
        req.addProperty("fuel", fuel);
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
