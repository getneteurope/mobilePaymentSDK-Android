/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */

package com.sdkpay.ecom.examples;

import android.util.Base64;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class SignatureHelper {

    private static final String ENCRYPTION_ALGORITHM = "HS256";
    private static final String UTF_8 = "UTF-8";

    public static String generateTimestamp() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(timeZone);

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     *  Sample for generating signature according to input
     *  Note that signature should be generated server side and should not be generated in the app because of security matters
     */
    public static String generateSignature(String timestamp, String merchantID, String requestID,
                                           String transactionType, BigDecimal amount, String currency, String secretKey) {


        String payload = ENCRYPTION_ALGORITHM.toUpperCase() + "\n" +
                "request_time_stamp=" + timestamp + "\n" +
                "merchant_account_id=" + merchantID + "\n" +
                "request_id=" + requestID + "\n" +
                "transaction_type=" + transactionType;
        if(amount != null && currency != null){
            payload +=  "\n" +"requested_amount=" + amount +
                        "\n" + "requested_amount_currency=" + currency.toUpperCase();
        }

        try {
            byte[] encryptedPayload = encryptSignature(payload, secretKey);
            return new String(Base64.encode(payload.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP), StandardCharsets.UTF_8)
                    + "." + new String(Base64.encode(encryptedPayload, Base64.NO_WRAP), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encryptSignature(String payload, String secretKey) {
        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA256"));
            return mac.doFinal(payload.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[1];
    }
}
