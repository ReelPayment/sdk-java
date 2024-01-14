<p align="center">
    <a href="https://reelpay.com/" target="_blank" rel="external">
        <img src="https://github.com/ReelPayment/sdk-java/blob/main/logo.png" height="100px">
    </a>
    <h1 align="center">ReelPay SDK-JAVA</h1>
    <br>
</p>

ReelPay SDK-JAVA

ReelPay is committed to helping users quickly transition from Web 2.0 to Web 3.0, attract new users, and collect mainstream currency protocol gateways like BTC, ETH, BSC, SOL, and TRX. Moreover, it aims to help safeguard payment security and provide services such as contract receiving, notifications for address receiving, QR Code receiving, and social promotion.

Documentation is at https://docs.reelpay.com/.

Requirements
------------
JDK: >= 1.8

Configuration
-------------

import com.google.gson.JsonObject;
import com.reelpay.api.Transactions;

public class Test {
    public static void main(String[] args) {
        String appId = "Your appid";
        String appKey = "Your appKey";

        // Hosted Checkout
        String out_trade_no = "20240101xxxxxxxxxxxxx";
        String symbol = "USD";
        String amount = "3.00";
        String name = "Buy coins";
        String image = "https://reelpay.com/product.jpg";

        Transactions transaction = new Transactions(appId, appKey);
        JsonObject rt = transaction.EntrustPay(out_trade_no, symbol, amount, name, image);
        System.out.println(rt.toString());
        //{"code":200,"message":"success","data":{"url":"https://pay.reelpay.com/df9fbe011ca2a1c70f20240112113339","time_expire":1705032219,"trade_no":""}}
    }
}


## Where can I get Appid and Appkey?
Register and open a merchant account, go to application <a href="https://merchant.reelpay.com/" target="_blank" rel="external">management</a> after login background, and create and apply for an application to get the corresponding Appied and Appkey.
