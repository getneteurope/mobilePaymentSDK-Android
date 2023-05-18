/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
 */

package com.sdkpay.ecom.examples.providers;

import static com.sdkpay.ecom.examples.Constants.URL_EE_TEST;

import com.sdkpay.ecom.alipay.model.AlipayPayment;
import com.sdkpay.ecom.bizum.model.BizumPayment;
import com.sdkpay.ecom.blik.model.BlikLevel0Payment;
import com.sdkpay.ecom.blik.model.BlikRedirectPayment;
import com.sdkpay.ecom.card.model.CardBundle;
import com.sdkpay.ecom.card.model.CardFieldPayment;
import com.sdkpay.ecom.card.model.CardPayment;
import com.sdkpay.ecom.examples.SignatureHelper;
import com.sdkpay.ecom.model.AccountHolder;
import com.sdkpay.ecom.model.Address;
import com.sdkpay.ecom.model.BankAccount;
import com.sdkpay.ecom.model.CardToken;
import com.sdkpay.ecom.model.Notification;
import com.sdkpay.ecom.model.Notifications;
import com.sdkpay.ecom.model.OrderItem;
import com.sdkpay.ecom.model.OrderItemType;
import com.sdkpay.ecom.model.RequestedAmount;
import com.sdkpay.ecom.model.Shipping;
import com.sdkpay.ecom.model.TransactionType;
import com.sdkpay.ecom.p24.model.P24Payment;
import com.sdkpay.ecom.paypal.model.PayPalPayment;
import com.sdkpay.ecom.ratepay.model.RatepayElvPayment;
import com.sdkpay.ecom.ratepay.model.RatepayInvoicePayment;
import com.sdkpay.ecom.sepa.model.SepaPayment;
import com.sdkpay.ecom.sofort.model.SofortPayment;
import com.sdkpay.ecom.wiretransfer.model.WiretransferPayment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class PaymentObjectProvider {
    OptionalFieldsProvider optionalFieldsProvider;

    public PaymentObjectProvider(OptionalFieldsProvider optionalFieldsProvider) {
        this.optionalFieldsProvider = optionalFieldsProvider;
    }

    public SepaPayment getSepaPaymentObject() {
        String timestamp = SignatureHelper.generateTimestamp();
        String merchantID = "98beb2e5-85af-4ba0-8aea-1e76b330eb6b";
        String secretKey = "eaeeb74a-bc12-4d6d-818e-92a8cdfd6b76";
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

    public CardPayment getCardPaymentWithOptionalData() {
        return optionalFieldsProvider.appendCardOptionalData(getCardPayment(false, false));
    }

    public CardPayment getCardPayment(boolean isAnimated) {
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
        if (append3dsV2Fields)
            return (CardPayment) optionalFieldsProvider.appendThreeDSV2Fields(cardPayment);
        else return cardPayment;
    }

    public CardPayment getCardTokenPayment() {
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
        cardToken.setTokenId("5500971586101006");
        cardToken.setMaskedAccountNumber("541333******1006");

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

    public PayPalPayment getPayPalPayment() {
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

    public AlipayPayment getAlipayPayment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "ALIPAY_MAID";
        String secretKey = "ALIPAY_SECRET";
        TransactionType transactionType = TransactionType.DEBIT;
        BigDecimal amount = new BigDecimal(5);
        String currency = "USD";
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

    public WiretransferPayment getWiretransferPayment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "9e035f8f-bf8d-4179-aafb-ca059128e089";
        String secretKey = "c3b39f09-5632-42fc-b1a6-2a1b386613dd";
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

    public P24Payment getP24Payment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "39525cf9-fe90-4f99-9ce2-42815754d7c1";
        String secretKey = "6951db91-063a-4f28-b88e-02bde3ae555e";
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

    public SofortPayment getSofortPayment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "cd553bc0-e8f1-4d4c-8a36-eb9b2040304f";
        String secretKey = "d069455a-9852-4494-95d1-5caedbdcf6ce";
        TransactionType transactionType = TransactionType.DEBIT;
        BigDecimal amount = new BigDecimal(10);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);

        SofortPayment payment = new SofortPayment.Builder()
                .setSignature(signature)
                .setRequestId(requestId)
                .setMerchantAccountId(merchantId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setAccountHolder(
                        new AccountHolder(
                                "Getnetbuyer", "Spintzyk", "sofort.buyer2@getneteurope.com"
                        )
                )
                .setOrderNumber("18040037092713285")
                .setDescriptor("DESC180099798888")
                .build();

        return payment;
    }

    public RatepayInvoicePayment getRatepayInvoicePayment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "1da76824-e28a-4b64-8f41-c74a0a0e0c0c";
        String secretKey = "fb9d2b5d-e89b-427b-be66-2c4704378908";
        TransactionType transactionType = TransactionType.AUTHORIZATION;
        BigDecimal amount = new BigDecimal(15);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);

        RatepayInvoicePayment payment = new RatepayInvoicePayment.Builder()
                .setSignature(signature)
                .setRequestId(requestId)
                .setMerchantAccountId(merchantId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setAccountHolder(getRatePayAccountHolder())
                .setOrderNumber(UUID.randomUUID().toString())
                .setOrderItems(getRatePayOrderItems())
                .setDeviceFingerprint("DeviceIdentToken")
                .build();

        return payment;
    }

    public RatepayElvPayment getRatepayElvPayment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "5289ba2d-6094-4f7d-b6c2-346d1af638eb";
        String secretKey = "bb3ec883-9c5b-4e70-beff-c3151b17ecf4";
        TransactionType transactionType = TransactionType.AUTHORIZATION;
        BigDecimal amount = new BigDecimal(15);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);
        BankAccount bankAccount = BankAccount.Companion.ibanBic(
                "Danske Bank Hamburg",
                "DE83203205004989123456",
                "DABADEHHXXX"
        );

        RatepayElvPayment payment = new RatepayElvPayment.Builder()
                .setSignature(signature)
                .setRequestId(requestId)
                .setMerchantAccountId(merchantId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setAccountHolder(getRatePayAccountHolder())
                .setShippingAddress(getRatePayShipping())
                .setOrderNumber(UUID.randomUUID().toString())
                .setOrderItems(getRatePayOrderItems())
                .setBankAccount(bankAccount)
                .setDeviceFingerprint("DeviceIdentToken")
                .setCreditorId("DE12345667787")
                .setMandateId("25874659562")
                .build();

        return payment;
    }


    private AccountHolder getRatePayAccountHolder() {
        AccountHolder ratePayAccountHolder = new AccountHolder();
        Address address = new Address();
        address.setStreet1("Nicht Versenden Str. 5");
        address.setCity("Testhausen");
        address.setState("Berlin");
        address.setCountry("DE");
        address.setPostalCode("13353");
        ratePayAccountHolder.setFirstName("Test");
        ratePayAccountHolder.setLastName("Wischnewski");
        ratePayAccountHolder.setEmail("test@test.com");
        ratePayAccountHolder.setGender("Male");
        ratePayAccountHolder.setPhone("03018425165");
        ratePayAccountHolder.setAddress(address);
        return ratePayAccountHolder;
    }

    private Shipping getRatePayShipping() {
        Shipping shipping = new Shipping();
        Address address = new Address();
        address.setStreet1("Nicht Versenden Str. 5");
        address.setCity("Testhausen");
        address.setState("Berlin");
        address.setCountry("DE");
        address.setPostalCode("13353");
        shipping.setFirstName("Hans-Jürgen");
        shipping.setLastName("Wischnewski");
        shipping.setPhone("+49123123123");
        shipping.setAddress(address);
        return shipping;
    }

    private ArrayList<OrderItem> getRatePayOrderItems() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("Item A");
        orderItem1.setArticleNumber("123");
        orderItem1.setAmount(new RequestedAmount("5.0", "EUR"));
        orderItem1.setTaxRate(new BigDecimal("0.0"));
        orderItem1.setQuantity(1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setName("Item B");
        orderItem2.setArticleNumber("456");
        orderItem2.setAmount(new RequestedAmount("10.0", "EUR"));
        orderItem2.setTaxRate(new BigDecimal("0.0"));
        orderItem2.setQuantity(1);

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setName("Shipping Cost");
        orderItem3.setArticleNumber("789");
        orderItem3.setAmount(new RequestedAmount("5.0", "EUR"));
        orderItem3.setTaxRate(new BigDecimal("0.0"));
        orderItem3.setQuantity(1);
        orderItem3.setType(OrderItemType.SHIPMENT_FEE);

        OrderItem orderItem4 = new OrderItem();
        orderItem4.setName("Discount");
        orderItem4.setArticleNumber("111");
        orderItem4.setAmount(new RequestedAmount("-5.0", "EUR"));
        orderItem4.setTaxRate(new BigDecimal("0.0"));
        orderItem4.setQuantity(1);
        orderItem4.setType(OrderItemType.DISCOUNT);

        ArrayList<OrderItem> items = new ArrayList<OrderItem>();
        items.add(orderItem1);
        items.add(orderItem2);
        items.add(orderItem3);
        items.add(orderItem4);
        return items;
    }

    private AccountHolder getBlikAccountHolder() {
        AccountHolder ratePayAccountHolder = new AccountHolder();
        ratePayAccountHolder.setEmail("test@test.com");
        return ratePayAccountHolder;
    }

    public BlikRedirectPayment getBlikRedirectPayment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "54c06d30-73a7-45b0-b1f6-6f5ae91cd40c";
        String secretKey = "285fe5af-09f0-4e99-b8a8-439f14d75e20";
        TransactionType transactionType = TransactionType.DEBIT;
        BigDecimal amount = new BigDecimal(15);
        String currency = "PLN";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);

        BlikRedirectPayment payment = new BlikRedirectPayment.Builder()
                .setSignature(signature)
                .setRequestId(requestId)
                .setMerchantAccountId(merchantId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setAccountHolder(getBlikAccountHolder())
                .setOrderNumber("18040037092713285")
                .setDescriptor("DESC180099798888")
                .build();

        return payment;
    }

    public BlikLevel0Payment getBlikLevel0Payment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "54c06d30-73a7-45b0-b1f6-6f5ae91cd40c";
        String secretKey = "285fe5af-09f0-4e99-b8a8-439f14d75e20";
        TransactionType transactionType = TransactionType.DEBIT;
        BigDecimal amount = new BigDecimal(15);
        String currency = "PLN";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);

        BlikLevel0Payment payment = new BlikLevel0Payment.Builder()
                .setSignature(signature)
                .setRequestId(requestId)
                .setMerchantAccountId(merchantId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setAccountHolder(getBlikAccountHolder())
                .setOrderNumber("18040037092713285")
                .setDescriptor("DESC180099798888")
                .build();

        return payment;
    }

    private AccountHolder getBizumAccountHolder() {
        AccountHolder ratePayAccountHolder = new AccountHolder();
        ratePayAccountHolder.setPhone("+34700000000");
        return ratePayAccountHolder;
    }
    public BizumPayment getBizumPayment() {
        String timestamp = SignatureHelper.generateTimestamp();
        String requestId = UUID.randomUUID().toString();
        String merchantId = "75237619-7053-439f-90c9-273b05cc254a";
        String secretKey = "6ce8c641-145b-4607-9f45-3a18dbe073a5";
        TransactionType transactionType = TransactionType.AUTHORIZATION;
        BigDecimal amount = new BigDecimal(15);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.getValue(), amount, currency, secretKey);

        BizumPayment payment = new BizumPayment.Builder()
                .setSignature(signature)
                .setRequestId(requestId)
                .setMerchantAccountId(merchantId)
                .setTransactionType(transactionType)
                .setAmount(amount)
                .setCurrency(currency)
                .setAccountHolder(getBizumAccountHolder())
                .setOrderNumber("18040037092713285")
                .setDescriptor("DESC180099798888")
                .build();

        return payment;
    }
}
