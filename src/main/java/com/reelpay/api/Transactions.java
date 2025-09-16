package com.reelpay.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.reelpay.api.HttpRequest.Client;
import com.reelpay.api.HttpRequest.Cover;
import com.reelpay.api.constants.ReelPayConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * ReelPay SDK Transactions API Client
 * 
 * This class provides methods to interact with ReelPay's transaction API,
 * including creating payment orders, querying order information, 
 * currency exchange, hosted checkout, and payout operations.
 * 
 * @author ReelPay SDK Team
 * @version 1.0.1
 */
public class Transactions extends Client {
    
    /** Default API base URL */
    private static final String DEFAULT_API_URL = ReelPayConstants.DEFAULT_API_URL;
    
    /** Logger instance for this class */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Constructor with default API URL
     * 
     * @param appId Application ID provided by ReelPay
     * @param appKey Application Key provided by ReelPay
     */
    public Transactions(String appId, String appKey) {
        super.baseUrl = DEFAULT_API_URL;
        super.appId = appId;
        super.appKey = appKey;
    }

    /**
     * Constructor with custom API URL
     * 
     * @param appId Application ID provided by ReelPay
     * @param appKey Application Key provided by ReelPay
     * @param apiUrl Custom API base URL
     */
    public Transactions(String appId, String appKey, String apiUrl) {
        super.baseUrl = apiUrl;
        super.appId = appId;
        super.appKey = appKey;
    }

    // ==================== Payment Order Creation Methods ====================
    
    /**
     * Create a payment order using JsonObject
     * 
     * @param requestData JsonObject containing order parameters
     * @return JsonObject API response containing order details
     */
    public JsonObject createOrder(JsonObject requestData) {
        return request(ReelPayConstants.ENDPOINT_CREATE_ORDER, requestData);
    }

    /**
     * Create a payment order using JSON string
     * 
     * @param jsonRequestString JSON string containing order parameters
     * @return JsonObject API response containing order details
     */
    public JsonObject createOrder(String jsonRequestString) {
        JsonObject requestData = JsonParser.parseString(jsonRequestString).getAsJsonObject();
        return createOrder(requestData);
    }

    /**
     * Create a basic payment order
     * 
     * @param outTradeNo Merchant's unique order ID
     * @param currencyId Cryptocurrency ID (e.g., "BTC", "ETH")
     * @param fiatName Fiat currency name (e.g., "USD", "EUR")
     * @param fiatAmount Amount in fiat currency
     * @return JsonObject API response containing order details
     */
    public JsonObject createOrder(String outTradeNo, String currencyId, String fiatName, String fiatAmount) {
        JsonObject requestData = buildOrderRequest(outTradeNo, currencyId, fiatName, fiatAmount, null, null, 0);
        return createOrder(requestData);
    }

    /**
     * Create a payment order with callback URL and timeout
     * 
     * @param outTradeNo Merchant's unique order ID
     * @param currencyId Cryptocurrency ID (e.g., "BTC", "ETH")
     * @param fiatName Fiat currency name (e.g., "USD", "EUR")
     * @param fiatAmount Amount in fiat currency
     * @param callbackUrl Callback URL for payment notifications
     * @param timeoutSeconds Order timeout in seconds
     * @return JsonObject API response containing order details
     */
    public JsonObject createOrder(String outTradeNo, String currencyId, String fiatName, String fiatAmount, 
                                 String callbackUrl, long timeoutSeconds) {
        JsonObject requestData = buildOrderRequest(outTradeNo, currencyId, fiatName, fiatAmount, callbackUrl, null, timeoutSeconds);
        logger.debug("Creating order with request: {}", requestData.toString());
        return createOrder(requestData);
    }

    /**
     * Create a payment order with callback and redirect URLs
     * 
     * @param outTradeNo Merchant's unique order ID
     * @param currencyId Cryptocurrency ID (e.g., "BTC", "ETH")
     * @param fiatName Fiat currency name (e.g., "USD", "EUR")
     * @param fiatAmount Amount in fiat currency
     * @param callbackUrl Callback URL for payment notifications
     * @param redirectUrl Redirect URL after payment completion
     * @return JsonObject API response containing order details
     */
    public JsonObject createOrder(String outTradeNo, String currencyId, String fiatName, String fiatAmount, 
                                 String callbackUrl, String redirectUrl) {
        JsonObject requestData = buildOrderRequest(outTradeNo, currencyId, fiatName, fiatAmount, callbackUrl, redirectUrl, 0);
        return createOrder(requestData);
    }

    /**
     * Build order request JsonObject with common parameters
     * 
     * @param outTradeNo Merchant's unique order ID
     * @param currencyId Cryptocurrency ID
     * @param fiatName Fiat currency name
     * @param fiatAmount Amount in fiat currency
     * @param callbackUrl Callback URL (optional)
     * @param redirectUrl Redirect URL (optional)
     * @param timeoutSeconds Timeout in seconds (0 if not specified)
     * @return JsonObject containing order parameters
     */
    private JsonObject buildOrderRequest(String outTradeNo, String currencyId, String fiatName, String fiatAmount,
                                       String callbackUrl, String redirectUrl, long timeoutSeconds) {
        JsonObject requestData = new JsonObject();
        requestData.addProperty("out_trade_no", outTradeNo);
        requestData.addProperty("currency_id", currencyId);
        requestData.addProperty("fiat_name", fiatName);
        requestData.addProperty("fiat_amount", fiatAmount);
        
        if (callbackUrl != null && !callbackUrl.isEmpty()) {
            requestData.addProperty("callback_url", callbackUrl);
        }
        
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            requestData.addProperty("redirect_url", redirectUrl);
        }
        
        if (timeoutSeconds > 0) {
            requestData.addProperty("time_out", timeoutSeconds);
        }
        
        return requestData;
    }

    // ==================== Currency Exchange Methods ====================
    
    /**
     * Get current exchange rate between cryptocurrency and fiat currency
     * 
     * @param requestData JsonObject containing exchange parameters
     * @return JsonObject API response containing exchange rate information
     */
    public JsonObject getExchangeRate(JsonObject requestData) {
        return request(ReelPayConstants.ENDPOINT_EXCHANGE_RATE, requestData);
    }

    /**
     * Get current exchange rate using JSON string
     * 
     * @param jsonRequestString JSON string containing exchange parameters
     * @return JsonObject API response containing exchange rate information
     */
    public JsonObject getExchangeRate(String jsonRequestString) {
        JsonObject requestData = JsonParser.parseString(jsonRequestString).getAsJsonObject();
        return getExchangeRate(requestData);
    }

    /**
     * Get current exchange rate for specific currency pair
     * 
     * @param currencyId Cryptocurrency ID (see https://docs.reelpay.com/payment-api/api-interface#token-list)
     * @param fiatName Fiat currency name (e.g., "USD", "EUR")
     * @param fiatAmount Amount in fiat currency to convert
     * @return JsonObject API response containing exchange rate information
     */
    public JsonObject getExchangeRate(String currencyId, String fiatName, String fiatAmount) {
        JsonObject requestData = new JsonObject();
        requestData.addProperty("currency_id", currencyId);
        requestData.addProperty("fiat_name", fiatName);
        requestData.addProperty("fiat_amount", fiatAmount);
        return getExchangeRate(requestData);
    }

    // ==================== Order Query Methods ====================
    
    /**
     * Query order information using JsonObject
     * 
     * @param requestData JsonObject containing query parameters
     * @return JsonObject API response containing order information
     */
    public JsonObject queryOrderInfo(JsonObject requestData) {
        return request(ReelPayConstants.ENDPOINT_QUERY_ORDER, requestData);
    }

    /**
     * Query order information by trade number
     * 
     * @param tradeNo ReelPay order ID
     * @return JsonObject API response containing order information
     */
    public JsonObject queryOrderInfo(String tradeNo) {
        JsonObject requestData = new JsonObject();
        requestData.addProperty("trade_no", tradeNo);
        return queryOrderInfo(requestData);
    }

    // ==================== Currency Information Methods ====================
    
    /**
     * Get supported cryptocurrency list
     * 
     * @return JsonObject API response containing supported currencies
     */
    public JsonObject getSupportedCurrencies() {
        return request(ReelPayConstants.ENDPOINT_CURRENCIES, new JsonObject());
    }

    /**
     * Get supported cryptocurrency list using JsonObject (for consistency)
     * 
     * @param requestData JsonObject (ignored, kept for API consistency)
     * @return JsonObject API response containing supported currencies
     */
    public JsonObject getSupportedCurrencies(JsonObject requestData) {
        return getSupportedCurrencies();
    }

    /**
     * Get supported cryptocurrency list using JSON string (for consistency)
     * 
     * @param jsonRequestString JSON string (ignored, kept for API consistency)
     * @return JsonObject API response containing supported currencies
     */
    public JsonObject getSupportedCurrencies(String jsonRequestString) {
        return getSupportedCurrencies();
    }

    // ==================== Hosted Checkout Methods ====================
    
    /**
     * Create hosted checkout using JsonObject
     * 
     * @param requestData JsonObject containing checkout parameters
     * @return JsonObject API response containing checkout URL and details
     */
    public JsonObject createHostedCheckout(JsonObject requestData) {
        return request(ReelPayConstants.ENDPOINT_HOSTED_CHECKOUT, requestData);
    }

    /**
     * Create hosted checkout using JSON string
     * 
     * @param jsonRequestString JSON string containing checkout parameters
     * @return JsonObject API response containing checkout URL and details
     */
    public JsonObject createHostedCheckout(String jsonRequestString) {
        JsonObject requestData = JsonParser.parseString(jsonRequestString).getAsJsonObject();
        return createHostedCheckout(requestData);
    }

    /**
     * Create basic hosted checkout
     * 
     * @param outTradeNo Merchant's unique order ID
     * @param symbol Legal currency unit (e.g., "USD", "EUR")
     * @param amount Product price (keep 2 decimal places, e.g., "2.00", "0.50")
     * @param name Product name
     * @param image Product image URL
     * @return JsonObject API response containing checkout URL and details
     */
    public JsonObject createHostedCheckout(String outTradeNo, String symbol, String amount, String name, String image) {
        JsonObject requestData = buildCheckoutRequest(outTradeNo, symbol, amount, name, image, 0, null, null);
        return createHostedCheckout(requestData);
    }

    /**
     * Create hosted checkout with additional parameters
     * 
     * @param outTradeNo Merchant's unique order ID
     * @param symbol Legal currency unit (e.g., "USD", "EUR")
     * @param amount Product price (keep 2 decimal places, e.g., "2.00", "0.50")
     * @param name Product name
     * @param image Product image URL
     * @param timeExpire Order expiration timestamp
     * @param description Product description
     * @param callbackUrl Callback URL for payment notifications
     * @return JsonObject API response containing checkout URL and details
     */
    public JsonObject createHostedCheckout(String outTradeNo, String symbol, String amount, String name, String image,
                                         int timeExpire, String description, String callbackUrl) {
        JsonObject requestData = buildCheckoutRequest(outTradeNo, symbol, amount, name, image, timeExpire, description, callbackUrl);
        return createHostedCheckout(requestData);
    }

    /**
     * Build hosted checkout request JsonObject
     * 
     * @param outTradeNo Merchant's unique order ID
     * @param symbol Legal currency unit
     * @param amount Product price
     * @param name Product name
     * @param image Product image URL
     * @param timeExpire Order expiration timestamp (0 if not specified)
     * @param description Product description (optional)
     * @param callbackUrl Callback URL (optional)
     * @return JsonObject containing checkout parameters
     */
    private JsonObject buildCheckoutRequest(String outTradeNo, String symbol, String amount, String name, String image,
                                          int timeExpire, String description, String callbackUrl) {
        JsonObject requestData = new JsonObject();
        requestData.addProperty("out_trade_no", outTradeNo);
        requestData.addProperty("symbol", symbol);
        requestData.addProperty("amount", amount);
        requestData.addProperty("name", name);
        requestData.addProperty("image", image);
        
        if (timeExpire > 0) {
            requestData.addProperty("time_expire", timeExpire);
        }
        
        if (description != null && !description.isEmpty()) {
            requestData.addProperty("describe", description);
        }
        
        if (callbackUrl != null && !callbackUrl.isEmpty()) {
            requestData.addProperty("callback_url", callbackUrl);
        }
        
        return requestData;
    }

    // ==================== Payout Methods ====================
    
    /**
     * Create payout using JsonObject
     * 
     * @param requestData JsonObject containing payout parameters
     * @return JsonObject API response containing payout details
     */
    public JsonObject createPayout(JsonObject requestData) {
        return request(ReelPayConstants.ENDPOINT_CREATE_PAYOUT, requestData);
    }

    /**
     * Create payout using JSON string
     * 
     * @param jsonRequestString JSON string containing payout parameters
     * @return JsonObject API response containing payout details
     */
    public JsonObject createPayout(String jsonRequestString) {
        JsonObject requestData = JsonParser.parseString(jsonRequestString).getAsJsonObject();
        return createPayout(requestData);
    }

    /**
     * Create payout to transfer funds to a third-party address
     * 
     * @param currencyId Cryptocurrency ID
     * @param amount Amount to transfer
     * @param toAddress Destination address
     * @param googleCode Google Authenticator code (if required)
     * @param callbackUrl Callback URL for payout notifications
     * @return JsonObject API response containing payout details
     */
    public JsonObject createPayout(String currencyId, String amount, String toAddress, String googleCode, String callbackUrl) {
        JsonObject requestData = new JsonObject();
        requestData.addProperty("currency_id", currencyId);
        requestData.addProperty("to_address", toAddress);
        requestData.addProperty("amount", amount);
        requestData.addProperty("google_code", googleCode);
        requestData.addProperty("callback_url", callbackUrl);
        return createPayout(requestData);
    }

    // ==================== Payout Query Methods ====================
    
    /**
     * Query payout information using JsonObject
     * 
     * @param requestData JsonObject containing query parameters
     * @return JsonObject API response containing payout status
     */
    public JsonObject queryPayoutInfo(JsonObject requestData) {
        return request(ReelPayConstants.ENDPOINT_QUERY_PAYOUT, requestData);
    }

    /**
     * Query payout status by trade number
     * 
     * @param tradeNo Payout trade number
     * @return JsonObject API response containing payout status
     */
    public JsonObject queryPayoutInfo(String tradeNo) {
        JsonObject requestData = new JsonObject();
        requestData.addProperty("trade_no", tradeNo);
        return queryPayoutInfo(requestData);
    }

    // ==================== Callback Verification Methods ====================
    
    /**
     * Verify callback signature to ensure the request is from ReelPay
     * 
     * Important: Always verify the order status and amount in your callback handler
     * in addition to signature verification.
     * 
     * @param requestBody Raw request body as string
     * @param requestHeaders Request headers containing signature information
     * @return true if signature is valid, false otherwise
     */
    public boolean verifyCallbackSignature(String requestBody, HashMap<String, String> requestHeaders) {
        try {
            String appId = requestHeaders.get(ReelPayConstants.HEADER_APP_ID);
            String timestamp = requestHeaders.get(ReelPayConstants.HEADER_TIMESTAMP);
            String signature = requestHeaders.get(ReelPayConstants.HEADER_SIGNATURE);
            
            // Validate required headers
            if (appId == null || timestamp == null || signature == null) {
                logger.error(ReelPayConstants.ERROR_MISSING_HEADERS);
                return false;
            }
            
            // Verify app ID
            if (!this.appId.equals(appId)) {
                logger.error(ReelPayConstants.ERROR_APP_ID_MISMATCH);
                return false;
            }
            
            // Verify signature
            Cover cover = new Cover(this.appKey, requestBody, Long.valueOf(timestamp));
            if (!cover.validateSign(signature)) {
                logger.error(ReelPayConstants.ERROR_SIGNATURE_VALIDATION_FAILED);
                return false;
            }
            
            logger.debug(ReelPayConstants.SUCCESS_SIGNATURE_VERIFIED);
            return true;
            
        } catch (Exception e) {
            logger.error("Error during callback signature verification: {}", e.getMessage(), e);
            return false;
        }
    }

    // ==================== Utility Methods ====================
    
    /**
     * Get supported transaction statuses
     * 
     * @return Array of supported transaction status strings
     */
    public String[] getSupportedTransactionStatuses() {
        return ReelPayConstants.getSupportedTransactionStatuses();
    }

    /**
     * Check if a transaction status is supported
     * 
     * @param status Transaction status to check
     * @return true if status is supported, false otherwise
     */
    public boolean isValidTransactionStatus(String status) {
        return ReelPayConstants.isValidTransactionStatus(status);
    }
}
