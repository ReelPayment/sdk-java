package com.reelpay.api;


import com.google.gson.JsonObject;

public class Main {
    public static void main(String[] args) {
        String appId = "Your appid";
        String appKey = "Your appKey";

        // Hosted Checkout
        String out_trade_no = "20240101xxxxxxxxxxxxx";
        String symbol = "USD";
        String amount = "3.00";
        String name = "Buy coins";
        String image = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";

        Transactions ta = new Transactions(appId, appKey);
        JsonObject rt = ta.EntrustPay(out_trade_no, symbol, amount, name, image);
        System.out.println(rt.toString());
        //{"code":200,"message":"success","data":{"url":"https://pay.reelpay.com/df9fbe011ca2a1c70f20240112113339","time_expire":1705032219,"trade_no":""}}
    }
}
