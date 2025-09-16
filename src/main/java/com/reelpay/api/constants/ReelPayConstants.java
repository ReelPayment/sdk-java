package com.reelpay.api.constants;

/**
 * ReelPay SDK Constants
 * 
 * This class contains all the constants used throughout the ReelPay SDK,
 * including API endpoints, transaction statuses, and configuration values.
 * 
 * @author ReelPay SDK Team
 * @version 1.0.1
 */
public final class ReelPayConstants {
    
    // Private constructor to prevent instantiation
    private ReelPayConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== API Configuration ====================
    
    /** Default API base URL */
    public static final String DEFAULT_API_URL = "https://api.reelpay.com";
    
    /** SDK version */
    public static final String SDK_VERSION = "1.0.1";
    
    /** User agent string for HTTP requests */
    public static final String USER_AGENT = "ReelPay-SDK-Java/" + SDK_VERSION;
    
    /** Default content type for API requests */
    public static final String CONTENT_TYPE_JSON = "application/json";
    
    // ==================== API Endpoints ====================
    
    /** Payment order creation endpoint */
    public static final String ENDPOINT_CREATE_ORDER = "/v1/transactions/pay";
    
    /** Exchange rate query endpoint */
    public static final String ENDPOINT_EXCHANGE_RATE = "/v1/transactions/amount";
    
    /** Order information query endpoint */
    public static final String ENDPOINT_QUERY_ORDER = "/v1/transactions";
    
    /** Supported currencies endpoint */
    public static final String ENDPOINT_CURRENCIES = "/v1/transactions/currency";
    
    /** Hosted checkout endpoint */
    public static final String ENDPOINT_HOSTED_CHECKOUT = "/v1/transactions/entrust";
    
    /** Payout creation endpoint */
    public static final String ENDPOINT_CREATE_PAYOUT = "/v1/transactions/transfer";
    
    /** Payout query endpoint */
    public static final String ENDPOINT_QUERY_PAYOUT = "/v1/transactions/getTransfer";
    
    // ==================== HTTP Headers ====================
    
    /** Application ID header */
    public static final String HEADER_APP_ID = "X-Appid";
    
    /** Timestamp header */
    public static final String HEADER_TIMESTAMP = "X-Timestamp";
    
    /** Signature header */
    public static final String HEADER_SIGNATURE = "X-Sign";
    
    /** Content type header */
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    
    /** User agent header */
    public static final String HEADER_USER_AGENT = "User-Agent";
    
    // ==================== Transaction Statuses ====================
    
    /** Transaction status: Paid */
    public static final String STATUS_PAID = "PAID";
    
    /** Transaction status: Time out */
    public static final String STATUS_TIME_OUT = "TIME-OUT";
    
    /** Transaction status: Closed */
    public static final String STATUS_CLOSE = "CLOSE";
    
    /** Transaction status: Refund in progress */
    public static final String STATUS_REFUND_IN_PROGRESS = "REFUND-IN-PROGRESS";
    
    /** Transaction status: Refunded */
    public static final String STATUS_REFUNDED = "REFUNDED";
    
    /** Transaction status: Abnormal */
    public static final String STATUS_ABNORMAL = "ABNORMAL";
    
    /** Transaction status: Chain confirmation failed */
    public static final String STATUS_CHAIN_CONFIRMATION_FAILED = "CHAIN-CONFIRMATION-FAILED";
    
    // ==================== Common Fiat Currencies ====================
    
    /** US Dollar */
    public static final String FIAT_USD = "USD";
    
    // ==================== Error Messages ====================
    
    /** Error message for signature validation failure */
    public static final String ERROR_SIGNATURE_VALIDATION_FAILED = "Signature validation failed";
    
    /** Error message for missing required headers */
    public static final String ERROR_MISSING_HEADERS = "Missing required headers";
    
    /** Error message for app ID mismatch */
    public static final String ERROR_APP_ID_MISMATCH = "App ID mismatch";
    
    /** Success message for signature verification */
    public static final String SUCCESS_SIGNATURE_VERIFIED = "Signature verification successful";
    
    // ==================== Utility Methods ====================
    
    /**
     * Get all supported transaction statuses
     * 
     * @return Array of supported transaction status strings
     */
    public static String[] getSupportedTransactionStatuses() {
        return new String[] {
            STATUS_PAID,
            STATUS_TIME_OUT,
            STATUS_CLOSE,
            STATUS_REFUND_IN_PROGRESS,
            STATUS_REFUNDED,
            STATUS_ABNORMAL,
            STATUS_CHAIN_CONFIRMATION_FAILED
        };
    }
    
    /**
     * Check if a transaction status is supported
     * 
     * @param status Transaction status to check
     * @return true if status is supported, false otherwise
     */
    public static boolean isValidTransactionStatus(String status) {
        if (status == null) {
            return false;
        }
        
        String[] supportedStatuses = getSupportedTransactionStatuses();
        for (String supportedStatus : supportedStatuses) {
            if (supportedStatus.equals(status)) {
                return true;
            }
        }
        return false;
    }
    
}
