/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
 */

package com.sdkpay.ecom.examples.providers;

import com.sdkpay.ecom.card.model.CardFieldPayment;
import com.sdkpay.ecom.card.model.CardPayment;
import com.sdkpay.ecom.model.AccountHolder;
import com.sdkpay.ecom.model.AccountInfo;
import com.sdkpay.ecom.model.Address;
import com.sdkpay.ecom.model.AuthenticationType;
import com.sdkpay.ecom.model.AvailabilityTime;
import com.sdkpay.ecom.model.BasePayment;
import com.sdkpay.ecom.model.ChallengeIndicator;
import com.sdkpay.ecom.model.DeliveryTimeframe;
import com.sdkpay.ecom.model.Gift;
import com.sdkpay.ecom.model.IsoTransactionType;
import com.sdkpay.ecom.model.LoyaltyCard;
import com.sdkpay.ecom.model.MerchantRiskIndicator;
import com.sdkpay.ecom.model.Notification;
import com.sdkpay.ecom.model.Notifications;
import com.sdkpay.ecom.model.OrderItem;
import com.sdkpay.ecom.model.Phone;
import com.sdkpay.ecom.model.ReorderType;
import com.sdkpay.ecom.model.RequestedAmount;
import com.sdkpay.ecom.model.Shipping;
import com.sdkpay.ecom.model.ShippingMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OptionalFieldsProvider {

    public BasePayment appendThreeDSV2Fields(BasePayment payment){

        if(payment instanceof CardPayment){
            ((CardPayment)payment).setAttempt3d(true);
            ((CardPayment)payment).setThreeDVersion("2.1.0");
        }

        if(payment instanceof CardFieldPayment){
            ((CardFieldPayment)payment).setAttempt3d(true);
            ((CardFieldPayment)payment).setThreeDVersion("2.1.0");
        }

        AccountHolder accountHolder = new AccountHolder();
        accountHolder.setFirstName("John");
        accountHolder.setLastName("Doe");
        accountHolder.setEmail("john.doe@email.com");
        AccountInfo accountInfo = new AccountInfo();

        accountInfo.setAuthenticationTimestamp(new Date(System.currentTimeMillis()));
        accountInfo.setAuthenticationType(AuthenticationType.NO_3DS_REQUESTOR_AUTHENTICATION_OCCURRED);
        accountInfo.setCardCreationDate(new Date());
        accountInfo.setCardTransactionsLastDay(10);
        accountInfo.setChallengeIndicator(ChallengeIndicator.CHALLENGE_REQUESTED_3DS_REQUESTOR_PREFERENCE);
        accountInfo.setPasswordChangeDate(new Date());
        accountInfo.setSuspiciousActivity(false);
        accountInfo.setTransactionsLastDay(20);
        accountInfo.setCreationDate(new Date());
        accountInfo.setTransactionsLastYear(9999);
        accountInfo.setShippingAddressFirstUse(new Date());
        accountInfo.setUpdateDate(new Date());
        accountInfo.setPurchasesLastSixMonths(3333);

        payment.setIsoTransactionType(IsoTransactionType.QUASI_CASH_TRANSACTION);

        MerchantRiskIndicator merchantRiskIndicator = new MerchantRiskIndicator();
        merchantRiskIndicator.setAvailability(AvailabilityTime.FUTURE_AVAILABILITY);
        merchantRiskIndicator.setDeliveryMail("aaaa");
        merchantRiskIndicator.setDeliveryTimeframe(DeliveryTimeframe.ELECTRONIC_DELIVERY);
        merchantRiskIndicator.setGift(new Gift(new RequestedAmount(new BigDecimal(5), "EUR"), 10));
        merchantRiskIndicator.setPreorderDate(new Date());
        merchantRiskIndicator.setReorderItems(ReorderType.FIRST_TIME);
        payment.setMerchantRiskIndicator(merchantRiskIndicator);

        Phone phone = new Phone();
        phone.setCountryPart("34343");
        phone.setOtherPart("535353");
        accountHolder.setHomePhone(phone);
        accountHolder.setMobilePhone(phone);
        accountHolder.setWorkPhone(phone);

        Shipping shipping = new Shipping();
        shipping.setShippingCity("lala");
        shipping.setShippingCountry("lala");
        shipping.setShippingPostalCode("lala");
        shipping.setShippingState("lala");
        shipping.setShippingStreet1("lala");
        shipping.setShippingStreet2("lala");
        shipping.setShippingStreet3("lala");
        shipping.setShippingMethod(ShippingMethod.SHIPPING_TO_STORE);
        payment.setShippingAddress(shipping);


        accountHolder.setAccountInfo(accountInfo);
        payment.setAccountHolder(accountHolder);

        return payment;
    }

    public CardPayment appendCardOptionalData(CardPayment cardPayment){
        // account holder information
        Address address = new Address();
        address.setStreet1("Einsteinring 35");
        address.setCity("Munich");
        address.setPostalCode("80331");
        address.setCountry("DE");

        AccountHolder accountHolder = new AccountHolder("John", "Doe");
        accountHolder.setDateOfBirth("1986-01-01");
        accountHolder.setAddress(address);

        cardPayment.setAccountHolder(accountHolder);

        // IP address
        cardPayment.setIpAddress("127.0.0.1");

        // loyalty card
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setUserId("7fe54e18-f251-4130-9f6b-0536afc86ee7");
        loyaltyCard.setCardId("32be4a4e-67ca-e911-b9b9-005056ab64a1");
        cardPayment.setLoyaltyCard(loyaltyCard);

        // notifications
        Notification notification = new Notification();
        notification.setUrl("https://server.com");

        List<Notification> listOfNotifications = new ArrayList<>();
        listOfNotifications.add(notification);

        Notifications notifications = new Notifications();
        notifications.setNotifications(listOfNotifications);
        notifications.setFormat(Notifications.FORMAT_JSON);

        cardPayment.setNotifications(notifications);

        // order
        OrderItem orderItem = new OrderItem();
        orderItem.setName("Bicycle");
        orderItem.setDescription("Best product ever");
        orderItem.setArticleNumber("BC1234");
        orderItem.setAmount(new RequestedAmount(new BigDecimal(800), "EUR"));
        orderItem.setQuantity(1);
        orderItem.setTaxRate(new BigDecimal(20));

        ArrayList<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        cardPayment.setOrderDetail("Test product");
        cardPayment.setOrderNumber("DE11111111");
        cardPayment.setOrderItems(orderItems);
        cardPayment.setOrderItems(orderItems);

        // shipping address
        Shipping shipping = new Shipping();
        shipping.setShippingCity("Berlin");
        shipping.setShippingCountry("DE");
        shipping.setShippingPostalCode("04001");
        shipping.setShippingState("Shipped");
        shipping.setShippingStreet1("Street 1");
        shipping.setShippingStreet2("Street 2 info");
        shipping.setShippingStreet3("Street 3 info");
        shipping.setShippingMethod(ShippingMethod.SHIPPING_TO_STORE);
        cardPayment.setShippingAddress(shipping);

        // descriptor
        cardPayment.setDescriptor("Demo descriptor");

        return cardPayment;
    }
}
