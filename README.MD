# ReelPay SDK Java

## Overview

<p align="center">
    <a href="https://reelpay.com/" target="_blank" rel="external">
        <img src="https://github.com/ReelPayment/sdk-java/blob/main/logo.png" height="100px">
    </a>
    <h1 align="center">ReelPay SDK-JAVA</h1>
    <br>
</p>

ReelPay is committed to helping users quickly transition from Web 2.0 to Web 3.0, attract new users, and collect mainstream currency protocol gateways like BTC, ETH, BSC, SOL, and TRX. Moreover, it aims to help safeguard payment security and provide services such as contract receiving, notifications for address receiving, QR Code receiving, and social promotion.

Documentation is at https://docs.reelpay.com/.

## Requirements

- **Java**: 8+
- **SDK Version**: 1.0.1


## Quick Start

### 1. Add Dependencies

```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.9</version>
    </dependency>
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>4.9.3</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.17.2</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.17.2</version>
    </dependency>
</dependencies>
```

### 2. Basic Usage

```java
import com.reelpay.api.Transactions;
import com.google.gson.JsonObject;

// Initialize SDK
Transactions transactions = new Transactions("your_app_id", "your_app_key");

// Create a payment order
JsonObject order = transactions.createOrder(
    "ORDER_123",    // Your order ID
    "currency_id",  // Get from getSupportedCurrencies()
    "USD",          // Fiat currency
    "10.00"         // Amount
);

System.out.println(order.toString());
```

## File Structure

```
src/main/java/com/reelpay/api/
├── Transactions.java              # Main API client class
├── Main.java                      # Example usage
├── constants/
│   └── ReelPayConstants.java      # Constants definition
├── HttpRequest/
│   ├── Client.java               # HTTP client base class
│   └── Cover.java                # Signature handling class
└── util/
    └── HmacUtil.java             # HMAC encryption utility class
```

## API Methods

### Get Supported Currencies
```java
JsonObject currencies = transactions.getSupportedCurrencies();
```

### Create Payment Order
```java
JsonObject order = transactions.createOrder(
    "ORDER_123", "currency_id", "USD", "10.00"
);
```

### Query Order
```java
JsonObject orderInfo = transactions.queryOrderInfo("trade_no");
```

### Get Exchange Rate
```java
JsonObject rate = transactions.getExchangeRate("currency_id", "USD", "1.00");
```

### Hosted Checkout
```java
JsonObject checkout = transactions.createHostedCheckout(
    "ORDER_123", "USD", "10.00", "Product Name", "image_url"
);
```

### Create Payout
```java
JsonObject payout = transactions.createPayout(
    "currency_id", "0.001", "address", "auth_code", "callback_url"
);
```

### Verify Callback
```java
HashMap<String, String> headers = new HashMap<>();
headers.put("X-Appid", "your_app_id");
headers.put("X-Timestamp", "timestamp");
headers.put("X-Sign", "signature");

boolean isValid = transactions.verifyCallbackSignature(requestBody, headers);
```

## Constants Usage

```java
import com.reelpay.api.constants.ReelPayConstants;

// Use predefined currency constants
String fiatCurrency = ReelPayConstants.FIAT_USD;

// Use API endpoint constants
String endpoint = ReelPayConstants.ENDPOINT_CREATE_ORDER;

// Use HTTP header constants
String appIdHeader = ReelPayConstants.HEADER_APP_ID;

// Check transaction status
boolean isValidStatus = ReelPayConstants.isValidTransactionStatus("PAID");
```

## Error Handling

All API methods throw `RuntimeException`, it's recommended to use try-catch blocks:

```java
try {
    JsonObject result = transactions.createOrder(...);
    // Handle success result
} catch (RuntimeException e) {
    // Handle error
    System.err.println("API call failed: " + e.getMessage());
}
```

## Logging

The SDK uses Log4j for logging. Dependencies are included in the Quick Start section above.

## Security Considerations

1. **Protect Keys**: Never hardcode API keys in your code
2. **Verify Callbacks**: Always verify callback signatures and order status
3. **Use HTTPS**: Use HTTPS in production environments
4. **Timeout Settings**: Set reasonable request timeout values

## Examples

Check the `Main.java` file for complete usage examples.

## Support

For any issues, please contact the ReelPay technical support team.
