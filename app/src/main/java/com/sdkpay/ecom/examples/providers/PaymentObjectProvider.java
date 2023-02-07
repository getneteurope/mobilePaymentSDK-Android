/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
 */

package com.sdkpay.ecom.examples.providers;

import static com.sdkpay.ecom.examples.Constants.URL_EE_TEST;

import com.sdkpay.ecom.alipay.model.AlipayPayment;
import com.sdkpay.ecom.card.model.CardBundle;
import com.sdkpay.ecom.card.model.CardFieldPayment;
import com.sdkpay.ecom.card.model.CardPayment;
import com.sdkpay.ecom.examples.SignatureHelper;
import com.sdkpay.ecom.model.AccountHolder;
import com.sdkpay.ecom.model.CardToken;
import com.sdkpay.ecom.model.Notification;
import com.sdkpay.ecom.model.Notifications;
import com.sdkpay.ecom.model.OrderItem;
import com.sdkpay.ecom.model.OrderItemType;
import com.sdkpay.ecom.model.RequestedAmount;
import com.sdkpay.ecom.model.TransactionType;
import com.sdkpay.ecom.p24.model.P24Payment;
import com.sdkpay.ecom.paypal.model.PayPalPayment;
import com.sdkpay.ecom.sepa.model.SepaPayment;
import com.sdkpay.ecom.wiretransfer.model.WiretransferPayment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class PaymentObjectProvider {
    OptionalFieldsProvider optionalFieldsProvider;

    public PaymentObjectProvider(OptionalFieldsProvider optionalFieldsProvider){
        this.optionalFieldsProvider = optionalFieldsProvider;
    }

    public SepaPayment getSepaPaymentObject() {
        String timestamp = SignatureHelper.generateTimestamp();
        String merchantID = "5c4ef296-46c5-482c-9143-509d13fcca34";
        String secretKey = "d60f0ba0-35cd-43f9-a0ef-1dab7880b1d7";
        String requestID = UUID.randomUUID().toString();
        TransactionType transactionType = TransactionType.AUTHORIZATION;
        BigDecimal amount = new BigDecimal(5);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.getValue(), amount, currency, secretKey);

        return new SepaPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantID)
                .setRequestId(requestID)
                .setAmount(amount)
                .setTransactionType(transactionType)
                .setCurrency(currency)
                .build();
    }

    public CardPayment getCardPaymentWithOptionalData(){
        return optionalFieldsProvider.appendCardOptionalData(getCardPayment(false, false));
    }

    public CardPayment getCardPayment(boolean isAnimated){
        return this.getCardPayment(isAnimated, true);
    }

    public CardPayment getCardPayment(boolean isAnimated, boolean append3dsV2Fields) {
        String timestamp = SignatureHelper.generateTimestamp();
        String merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108";
        String secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53";
        String requestID = UUID.randomUUID().toString();
        TransactionType transactionType = TransactionType.PURCHASE;
        BigDecimal amount = new BigDecimal(5);
        String currency = "EUR";

        // Application shall get signature from server where signature shall be computed
        String signature = SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.getValue(), amount, currency, secretKey);

        CardPayment cardPayment = new CardPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantID)
                .setRequestId(requestID)
                .setAmount(amount)
                .setTransactionType(transactionType)
                .setCurrency(currency)
                .build();
        cardPayment.setRequireManualCardBrandSelection(true);
        cardPayment.setAnimatedCardPayment(isAnimated);
        if(append3dsV2Fields)
            return (CardPayment) optionalFieldsProvider.appendThreeDSV2Fields(cardPayment);
        else return cardPayment;
    }

    public CardPayment getCardTokenPayment(){
        String timestamp = SignatureHelper.generateTimestamp();
        String merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108";
        String secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53";
        String requestID = UUID.randomUUID().toString();
        TransactionType transactionType = TransactionType.PURCHASE;
        BigDecimal amount = new BigDecimal(5);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.getValue(), amount, currency, secretKey);

        CardPayment cardPayment = new CardPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantID)
                .setRequestId(requestID)
                .setAmount(amount)
                .setTransactionType(transactionType)
                .setCurrency(currency)
                .build();
        cardPayment.setRequireManualCardBrandSelection(true);

        CardToken cardToken = new CardToken();
        cardToken.setTokenId("5524988500162004");
        cardToken.setMaskedAccountNumber("541333******2004");

        cardPayment.setCardToken(cardToken);

        return cardPayment;
    }

    public CardFieldPayment getCardFormPayment(CardBundle cardBundle) {
        String timestamp = SignatureHelper.generateTimestamp();
        String merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108";
        String secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53";
        String requestID = UUID.randomUUID().toString();
        TransactionType transactionType = TransactionType.PURCHASE;
        BigDecimal amount = new BigDecimal(5);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.getValue(), amount, currency, secretKey);

        CardFieldPayment cardFieldPayment = new CardFieldPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantID)
                .setRequestId(requestID)
                .setAmount(amount)
                .setTransactionType(transactionType)
                .setCurrency(currency)
                .setCardBundle(cardBundle)
                .build();

        cardFieldPayment.setAttempt3d(true);

        AccountHolder accountHolder = new AccountHolder("John", "Doe");
        cardFieldPayment.setAccountHolder(accountHolder);

        return (CardFieldPayment) optionalFieldsProvider.appendThreeDSV2Fields(cardFieldPayment);
    }

    public PayPalPayment getPayPalPayment(){
        String timestamp = SignatureHelper.generateTimestamp();
        String merchantID = "bb342082-9761-4481-802e-20b10d92545d";
        String secretKey = "fd1d35aa-952e-4549-9f3d-ea33c89c86c4";
        String requestID = UUID.randomUUID().toString();
        TransactionType transactionType = TransactionType.DEBIT;
        BigDecimal amount = new BigDecimal(5);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.getValue(), amount, currency, secretKey);

        PayPalPayment payPalPayment = new PayPalPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantID)
                .setRequestId(requestID)
                .setAmount(amount)
                .setTransactionType(transactionType)
                .setCurrency(currency)
                .setPeriodic(null)
                .setRiskReferenceId(null)
                .build();

        Notifications notifications = new Notifications();
        ArrayList<Notification> list = new ArrayList<>();
        Notification notification = new Notification();
        notification.setUrl(URL_EE_TEST + "/engine/mobile/v2/notify");
        list.add(notification);
        notifications.setNotifications(list);
        notifications.setFormat(Notifications.FORMAT_XML);
        payPalPayment.setNotifications(notifications);

        return payPalPayment;
    }

    public AlipayPayment getAlipayPayment(){
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "<to_be_updated>";
        String secretKey =  "<to_be_updated>";
        TransactionType transactionType = TransactionType.DEBIT;
        BigDecimal amount = new BigDecimal(5);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);

        AlipayPayment payment = new AlipayPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantId)
                .setRequestId(requestId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setLocale("EN")
                .build();

        return payment;
    }

    public WiretransferPayment getWiretransferPayment(){
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "9e035f8f-bf8d-4179-aafb-ca059128e089";
        String secretKey  = "c3b39f09-5632-42fc-b1a6-2a1b386613dd";
        TransactionType transactionType = TransactionType.AUTHORIZATION;
        BigDecimal amount = new BigDecimal(5);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);

        ArrayList<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        orderItem.setName("Test item");
        orderItem.setType(OrderItemType.SHIPMENT_FEE);
        orderItem.setAmount(new RequestedAmount(amount, currency));

        orderItems.add(orderItem);

        WiretransferPayment payment = new WiretransferPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantId)
                .setRequestId(requestId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setAccountHolder(new AccountHolder(null, "Test"))
                .build();

        return payment;
    }

    public P24Payment getP24Payment(){
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "39525cf9-fe90-4f99-9ce2-42815754d7c1";
        String secretKey =  "6951db91-063a-4f28-b88e-02bde3ae555e";
        TransactionType transactionType = TransactionType.DEBIT;
        BigDecimal amount = new BigDecimal(1);
        String currency = "PLN";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);

        ArrayList<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        orderItem.setName("Test item");
        orderItem.setType(OrderItemType.SHIPMENT_FEE);
        orderItem.setAmount(new RequestedAmount(amount, currency));

        orderItems.add(orderItem);

        P24Payment payment = new P24Payment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantId)
                .setRequestId(requestId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setAccountHolder(new AccountHolder(
                        null,
                        "Test",
                        "john@doe.test"
                ))
                .build();

        return payment;
    }

}
