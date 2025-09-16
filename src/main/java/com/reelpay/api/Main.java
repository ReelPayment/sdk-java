package com.reelpay.api;

import com.google.gson.JsonObject;
import com.reelpay.api.constants.ReelPayConstants;

/**
 * ReelPay SDK Example Usage
 * 
 * This class demonstrates how to use the ReelPay SDK for various operations
 * including creating orders, hosted checkout, currency exchange, and payouts.
 * 
 * Note: This is example code and should not be used in production.
 * Replace the hardcoded credentials with your actual ReelPay credentials.
 * 
 * @author ReelPay SDK Team
 * @version 1.0.1
 */
public class Main {

    // ==================== Configuration ====================
    
    /** Your ReelPay Application ID - Replace with your actual App ID */
    private static final String APP_ID = "your actual App ID";
    
    /** Your ReelPay Application Key - Replace with your actual App Key */
    private static final String APP_KEY = "your actual App Key";
    
    /** API URL - Use test URL for testing, production URL for live */
    private static final String API_URL = "https://api.reelpay.com"; // or test URL
    
    // ==================== Example Methods ====================

    /**
     * Main method demonstrating SDK usage
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Configure proxy if needed (remove in production)
        // configureProxy();
        
        try {
            // Run example operations
            demonstrateSupportedCurrencies();
            demonstrateHostedCheckout();
            String tradeNo = demonstrateCreateOrder();
            demonstrateCurrencyExchange();
            demonstrateQueryOrder(tradeNo);
            String payoutTradeNo = demonstratePayout();
            demonstrateQueryPayout(payoutTradeNo);
            demonstrateCallbackVerification();
            
        } catch (Exception e) {
            System.err.println("Example execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configure proxy settings (for testing only)
     */
    private static void configureProxy() {
        // Remove these lines in production
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "10800");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10800");
    }

    /**
     * Demonstrate hosted checkout functionality
     */
    private static void demonstrateHostedCheckout() {
        System.out.println("=== Hosted Checkout Example ===");
        
        try {
            Transactions transactions = new Transactions(APP_ID, APP_KEY, API_URL);
            
            String orderId = "ORDER_" + System.currentTimeMillis();
            String symbol = ReelPayConstants.FIAT_USD;
            String amount = "10.00";
            String productName = "Sample Product";
            String productImage = "https://example.com/product-image.jpg";
            int timeExpire = (int) (System.currentTimeMillis() / 1000) + 3600; // 1 hour from now
            String description = "This is a sample product for demonstration";
            String callbackUrl = "https://your-domain.com/callback";
            
            JsonObject result = transactions.createHostedCheckout(
                orderId, symbol, amount, productName, productImage, 
                timeExpire, description, callbackUrl
            );
            
            System.out.println("Hosted Checkout Result: " + result.toString());
            
        } catch (Exception e) {
            System.err.println("Hosted checkout failed: " + e.getMessage());
        }
    }

    /**
     * Demonstrate order creation
     * 
     * @return Trade number of created order
     */
    private static String demonstrateCreateOrder() {
        System.out.println("\n=== Create Order Example ===");
        
        try {
            Transactions transactions = new Transactions(APP_ID, APP_KEY, API_URL);
            
            // Get supported currencies first
            JsonObject currencies = transactions.getSupportedCurrencies();
            System.out.println("Supported Currencies: " + currencies.toString());
            
            // Extract first available currency
            String currencyId = currencies.getAsJsonArray("data")
                .get(0).getAsJsonObject()
                .get("currency_id").getAsString();
            
            String orderId = "ORDER_" + System.currentTimeMillis();
            String fiatName = ReelPayConstants.FIAT_USD;
            String fiatAmount = "5.00";
            String callbackUrl = "https://your-domain.com/callback";
            
            JsonObject result = transactions.createOrder(
                orderId, currencyId, fiatName, fiatAmount, callbackUrl, 3600L
            );
            
            System.out.println("Create Order Result: " + result.toString());
            
            // Extract trade number
            if (result.has("data") && result.getAsJsonObject("data").has("trade_no")) {
                return result.getAsJsonObject("data").get("trade_no").getAsString();
            }
            
        } catch (Exception e) {
            System.err.println("Create order failed: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Demonstrate supported currencies query
     */
    private static void demonstrateSupportedCurrencies() {
        System.out.println("\n=== Supported Currencies Example ===");
        
        try {
            Transactions transactions = new Transactions(APP_ID, APP_KEY, API_URL);
            
            JsonObject currencies = transactions.getSupportedCurrencies();
            System.out.println("Supported Currencies Result: " + currencies.toString());
            
            // Parse and display currency information
            if (currencies.has("data") && currencies.get("data").isJsonArray()) {
                System.out.println("\nAvailable Cryptocurrencies:");
                com.google.gson.JsonArray currencyArray = currencies.getAsJsonArray("data");
                for (int i = 0; i < currencyArray.size(); i++) {
                    com.google.gson.JsonObject currency = currencyArray.get(i).getAsJsonObject();
                    String currencyId = currency.has("currency_id") ? currency.get("currency_id").getAsString() : "N/A";
                    String chain = currency.has("chain") ? currency.get("chain").getAsString() : "N/A";
                    String token = currency.has("token") ? currency.get("token").getAsString() : "N/A";
                    System.out.println("  " + (i + 1) + ". " + chain + " + " + token + " + " + currencyId);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Get supported currencies failed: " + e.getMessage());
        }
    }

    /**
     * Demonstrate currency exchange rate query
     */
    private static void demonstrateCurrencyExchange() {
        System.out.println("\n=== Currency Exchange Example ===");
        
        try {
            Transactions transactions = new Transactions(APP_ID, APP_KEY, API_URL);
            
            // Get supported currencies first
            JsonObject currencies = transactions.getSupportedCurrencies();
            String currencyId = currencies.getAsJsonArray("data")
                .get(0).getAsJsonObject()
                .get("currency_id").getAsString();
            
            String fiatName = ReelPayConstants.FIAT_USD;
            String fiatAmount = "1.00";
            
            JsonObject result = transactions.getExchangeRate(currencyId, fiatName, fiatAmount);
            System.out.println("Exchange Rate Result: " + result.toString());
            
        } catch (Exception e) {
            System.err.println("Currency exchange failed: " + e.getMessage());
        }
    }

    /**
     * Demonstrate order query
     * 
     * @param tradeNo Trade number to query
     */
    private static void demonstrateQueryOrder(String tradeNo) {
        System.out.println("\n=== Query Order Example ===");
        
        if (tradeNo == null) {
            System.out.println("No trade number available for query");
            return;
        }
        
        try {
            Transactions transactions = new Transactions(APP_ID, APP_KEY, API_URL);
            JsonObject result = transactions.queryOrderInfo(tradeNo);
            System.out.println("Query Order Result: " + result.toString());
            
        } catch (Exception e) {
            System.err.println("Query order failed: " + e.getMessage());
        }
    }

    /**
     * Demonstrate payout creation
     * 
     * @return Payout trade number
     */
    private static String demonstratePayout() {
        System.out.println("\n=== Payout Example ===");
        
        try {
            Transactions transactions = new Transactions(APP_ID, APP_KEY, API_URL);
            
            String currencyId = "your_currency_id"; // Replace with actual currency ID
            String amount = "0.001";
            String toAddress = "recipient_wallet_address";
            String googleCode = ""; // Google Authenticator code if required
            String callbackUrl = "https://your-domain.com/payout-callback";
            
            JsonObject result = transactions.createPayout(
                currencyId, amount, toAddress, googleCode, callbackUrl
            );
            
            System.out.println("Payout Result: " + result.toString());
            
            // Extract payout trade number
            if (result.has("data") && result.getAsJsonObject("data").has("trade_no")) {
                return result.getAsJsonObject("data").get("trade_no").getAsString();
            }
            
        } catch (Exception e) {
            System.err.println("Payout failed: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Demonstrate payout query
     * 
     * @param payoutTradeNo Payout trade number to query
     */
    private static void demonstrateQueryPayout(String payoutTradeNo) {
        System.out.println("\n=== Query Payout Example ===");
        
        if (payoutTradeNo == null) {
            System.out.println("No payout trade number available for query");
            return;
        }
        
        try {
            Transactions transactions = new Transactions(APP_ID, APP_KEY, API_URL);
            JsonObject result = transactions.queryPayoutInfo(payoutTradeNo);
            System.out.println("Query Payout Result: " + result.toString());
            
        } catch (Exception e) {
            System.err.println("Query payout failed: " + e.getMessage());
        }
    }

    /**
     * Demonstrate callback signature verification
     */
    private static void demonstrateCallbackVerification() {
        System.out.println("\n=== Callback Verification Example ===");
        
        try {
            Transactions transactions = new Transactions(APP_ID, APP_KEY, API_URL);
            
            // Example callback data (replace with actual callback data)
            String callbackBody = "{\"trade_no\":\"example_trade_no\",\"status\":\"PAID\"}";
            java.util.HashMap<String, String> headers = new java.util.HashMap<>();
            headers.put(ReelPayConstants.HEADER_APP_ID, APP_ID);
            headers.put(ReelPayConstants.HEADER_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
            headers.put(ReelPayConstants.HEADER_SIGNATURE, "example_signature");
            
            boolean isValid = transactions.verifyCallbackSignature(callbackBody, headers);
            System.out.println("Callback signature valid: " + isValid);
            
            // Note: In real implementation, you would verify the actual signature
            // and also verify the order status and amount
            
        } catch (Exception e) {
            System.err.println("Callback verification failed: " + e.getMessage());
        }
    }
}
