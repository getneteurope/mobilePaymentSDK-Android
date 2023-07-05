/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */
package com.sdkpay.ecom.examples.providers

import com.sdkpay.ecom.card.model.CardFieldPayment
import com.sdkpay.ecom.card.model.CardPayment
import com.sdkpay.ecom.model.AccountHolder
import com.sdkpay.ecom.model.AccountInfo
import com.sdkpay.ecom.model.Address
import com.sdkpay.ecom.model.AuthenticationType
import com.sdkpay.ecom.model.AvailabilityTime
import com.sdkpay.ecom.model.BasePayment
import com.sdkpay.ecom.model.ChallengeIndicator
import com.sdkpay.ecom.model.DeliveryTimeframe
import com.sdkpay.ecom.model.Gift
import com.sdkpay.ecom.model.IsoTransactionType
import com.sdkpay.ecom.model.LoyaltyCard
import com.sdkpay.ecom.model.MerchantRiskIndicator
import com.sdkpay.ecom.model.Notification
import com.sdkpay.ecom.model.Notifications
import com.sdkpay.ecom.model.OrderItem
import com.sdkpay.ecom.model.Phone
import com.sdkpay.ecom.model.ReorderType
import com.sdkpay.ecom.model.RequestedAmount
import com.sdkpay.ecom.model.Shipping
import com.sdkpay.ecom.model.ShippingMethod
import java.math.BigDecimal
import java.util.Date

class OptionalFieldsProvider {
    fun appendThreeDSV2Fields(payment: BasePayment): BasePayment {
        if (payment is CardPayment) {
            payment.attempt3d = true
            payment.threeDVersion = "2.1.0"
        }
        if (payment is CardFieldPayment) {
            payment.attempt3d = true
            payment.threeDVersion = "2.1.0"
        }
        val accountHolder = AccountHolder()
        accountHolder.firstName = "John"
        accountHolder.lastName = "Doe"
        accountHolder.email = "john.doe@email.com"
        val accountInfo = AccountInfo()
        accountInfo.authenticationTimestamp = Date(System.currentTimeMillis())
        accountInfo.authenticationType = AuthenticationType.NO_3DS_REQUESTOR_AUTHENTICATION_OCCURRED
        accountInfo.cardCreationDate = Date()
        accountInfo.cardTransactionsLastDay = 10
        accountInfo.challengeIndicator = ChallengeIndicator.CHALLENGE_REQUESTED_3DS_REQUESTOR_PREFERENCE
        accountInfo.passwordChangeDate = Date()
        accountInfo.suspiciousActivity = false
        accountInfo.transactionsLastDay = 20
        accountInfo.creationDate = Date()
        accountInfo.transactionsLastYear = 9999
        accountInfo.shippingAddressFirstUse = Date()
        accountInfo.updateDate = Date()
        accountInfo.purchasesLastSixMonths = 3333
        payment.isoTransactionType = IsoTransactionType.QUASI_CASH_TRANSACTION
        val merchantRiskIndicator = MerchantRiskIndicator()
        merchantRiskIndicator.availability = AvailabilityTime.FUTURE_AVAILABILITY
        merchantRiskIndicator.deliveryMail = "aaaa"
        merchantRiskIndicator.deliveryTimeframe = DeliveryTimeframe.ELECTRONIC_DELIVERY
        merchantRiskIndicator.gift = Gift(RequestedAmount(BigDecimal(5), "EUR"), 10)
        merchantRiskIndicator.preorderDate = Date()
        merchantRiskIndicator.reorderItems = ReorderType.FIRST_TIME
        payment.merchantRiskIndicator = merchantRiskIndicator
        val phone = Phone()
        phone.countryPart = "34343"
        phone.otherPart = "535353"
        accountHolder.homePhone = phone
        accountHolder.mobilePhone = phone
        accountHolder.workPhone = phone
        val shipping = Shipping()
        shipping.shippingCity = "lala"
        shipping.shippingCountry = "lala"
        shipping.shippingPostalCode = "lala"
        shipping.shippingState = "lala"
        shipping.shippingStreet1 = "lala"
        shipping.shippingStreet2 = "lala"
        shipping.shippingStreet3 = "lala"
        shipping.shippingMethod = ShippingMethod.SHIPPING_TO_STORE
        payment.shippingAddress = shipping
        accountHolder.accountInfo = accountInfo
        payment.accountHolder = accountHolder
        return payment
    }

    fun appendCardOptionalData(cardPayment: CardPayment): CardPayment {
        // account holder information
        val address = Address()
        address.street1 = "Einsteinring 35"
        address.city = "Munich"
        address.postalCode = "80331"
        address.country = "DE"
        val accountHolder = AccountHolder("John", "Doe")
        accountHolder.dateOfBirth = "1986-01-01"
        accountHolder.address = address
        cardPayment.accountHolder = accountHolder

        // IP address
        cardPayment.ipAddress = "127.0.0.1"

        // loyalty card
        val loyaltyCard = LoyaltyCard()
        loyaltyCard.userId = "7fe54e18-f251-4130-9f6b-0536afc86ee7"
        loyaltyCard.cardId = "32be4a4e-67ca-e911-b9b9-005056ab64a1"
        cardPayment.loyaltyCard = loyaltyCard

        // notifications
        val notification = Notification()
        notification.url = "https://server.com"
        val listOfNotifications: MutableList<Notification> = ArrayList()
        listOfNotifications.add(notification)
        val notifications = Notifications()
        notifications.notifications = listOfNotifications
        notifications.format = Notifications.FORMAT_JSON
        cardPayment.notifications = notifications

        // order
        val orderItem = OrderItem()
        orderItem.name = "Bicycle"
        orderItem.description = "Best product ever"
        orderItem.articleNumber = "BC1234"
        orderItem.amount = RequestedAmount(BigDecimal(800), "EUR")
        orderItem.quantity = 1
        orderItem.taxRate = BigDecimal(20)
        val orderItems = ArrayList<OrderItem>()
        orderItems.add(orderItem)
        cardPayment.orderDetail = "Test product"
        cardPayment.orderNumber = "DE11111111"
        cardPayment.orderItems = orderItems
        cardPayment.orderItems = orderItems

        // shipping address
        val shipping = Shipping()
        shipping.shippingCity = "Berlin"
        shipping.shippingCountry = "DE"
        shipping.shippingPostalCode = "04001"
        shipping.shippingState = "Shipped"
        shipping.shippingStreet1 = "Street 1"
        shipping.shippingStreet2 = "Street 2 info"
        shipping.shippingStreet3 = "Street 3 info"
        shipping.shippingMethod = ShippingMethod.SHIPPING_TO_STORE
        cardPayment.shippingAddress = shipping

        // descriptor
        cardPayment.descriptor = "Demo descriptor"
        return cardPayment
    }
}
