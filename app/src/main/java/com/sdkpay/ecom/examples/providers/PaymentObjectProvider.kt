/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
 */
package com.sdkpay.ecom.examples.providers

import com.sdkpay.ecom.alipay.model.AlipayPayment
import com.sdkpay.ecom.bizum.model.BizumPayment
import com.sdkpay.ecom.blik.model.BlikLevel0Payment
import com.sdkpay.ecom.blik.model.BlikRedirectPayment
import com.sdkpay.ecom.card.model.CardBundle
import com.sdkpay.ecom.card.model.CardFieldPayment
import com.sdkpay.ecom.card.model.CardPayment
import com.sdkpay.ecom.examples.Constants.URL_EE_TEST
import com.sdkpay.ecom.examples.SignatureHelper
import com.sdkpay.ecom.ideal.model.IdealPayment
import com.sdkpay.ecom.mbreferencia.model.MbreferenciaPayment
import com.sdkpay.ecom.mbway.model.MbwayPayment
import com.sdkpay.ecom.model.AccountHolder
import com.sdkpay.ecom.model.Address
import com.sdkpay.ecom.model.BankAccount
import com.sdkpay.ecom.model.CardToken
import com.sdkpay.ecom.model.Notification
import com.sdkpay.ecom.model.Notifications
import com.sdkpay.ecom.model.OrderItem
import com.sdkpay.ecom.model.OrderItemType
import com.sdkpay.ecom.model.RequestedAmount
import com.sdkpay.ecom.model.Shipping
import com.sdkpay.ecom.model.TransactionType
import com.sdkpay.ecom.p24.model.P24Payment
import com.sdkpay.ecom.paypal.model.PayPalPayment
import com.sdkpay.ecom.ratepay.model.RatepayElvPayment
import com.sdkpay.ecom.ratepay.model.RatepayInvoicePayment
import com.sdkpay.ecom.sepa.model.SepaPayment
import com.sdkpay.ecom.sofort.model.SofortPayment
import com.sdkpay.ecom.wiretransfer.model.WiretransferPayment
import com.sdkpay.ecom.zinia.model.ZiniaPayment
import java.math.BigDecimal
import java.util.UUID

class PaymentObjectProvider(optionalFieldsProvider: com.sdkpay.ecom.examples.providers.OptionalFieldsProvider) {
    var optionalFieldsProvider: com.sdkpay.ecom.examples.providers.OptionalFieldsProvider

    init {
        this.optionalFieldsProvider = optionalFieldsProvider
    }

    fun getSepaPaymentObject(): SepaPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val merchantID = "98beb2e5-85af-4ba0-8aea-1e76b330eb6b"
        val secretKey = "eaeeb74a-bc12-4d6d-818e-92a8cdfd6b76"
        val requestID = UUID.randomUUID().toString()
        val transactionType: TransactionType = TransactionType.AUTHORIZATION
        val amount = BigDecimal(5)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.value, amount, currency, secretKey)
        return SepaPayment.Builder()
            .setSignature(signature)
            .setMerchantAccountId(merchantID)
            .setRequestId(requestID)
            .setAmount(amount)
            .setTransactionType(transactionType)
            .setCurrency(currency)
            .build()
    }

    fun getCardPaymentWithOptionalData(): CardPayment {
        return optionalFieldsProvider.appendCardOptionalData(getCardPayment(false, false))
    }

    fun getCardPayment(isAnimated: Boolean): CardPayment {
        return this.getCardPayment(isAnimated, true)
    }

    fun getCardPayment(isAnimated: Boolean, append3dsV2Fields: Boolean): CardPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108"
        val secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53"
        val requestID = UUID.randomUUID().toString()
        val transactionType: TransactionType = TransactionType.PURCHASE
        val amount = BigDecimal(5)
        val currency = "EUR"

        // Application shall get signature from server where signature shall be computed
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.value, amount, currency, secretKey)
        val cardPayment: CardPayment = CardPayment.Builder()
            .setSignature(signature)
            .setMerchantAccountId(merchantID)
            .setRequestId(requestID)
            .setAmount(amount)
            .setTransactionType(transactionType)
            .setCurrency(currency)
            .build()
        cardPayment.requireManualCardBrandSelection = true
        cardPayment.animatedCardPayment = isAnimated
        return if (append3dsV2Fields) optionalFieldsProvider.appendThreeDSV2Fields(cardPayment) as CardPayment else cardPayment
    }

    fun getCardTokenPayment(): CardPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108"
        val secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53"
        val requestID = UUID.randomUUID().toString()
        val transactionType: TransactionType = TransactionType.PURCHASE
        val amount = BigDecimal(5)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.value, amount, currency, secretKey)
        val cardPayment: CardPayment = CardPayment.Builder()
            .setSignature(signature)
            .setMerchantAccountId(merchantID)
            .setRequestId(requestID)
            .setAmount(amount)
            .setTransactionType(transactionType)
            .setCurrency(currency)
            .build()
        cardPayment.requireManualCardBrandSelection = true
        val cardToken = CardToken()
        cardToken.tokenId = "5427317855871006"
        cardToken.maskedAccountNumber = "541333******1006"
        cardPayment.cardToken = cardToken
        return cardPayment
    }

    fun getCardFormPayment(cardBundle: CardBundle): CardFieldPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108"
        val secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53"
        val requestID = UUID.randomUUID().toString()
        val transactionType: TransactionType = TransactionType.PURCHASE
        val amount = BigDecimal(5)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.value, amount, currency, secretKey)
        val cardFieldPayment: CardFieldPayment = CardFieldPayment.Builder()
            .setSignature(signature)
            .setMerchantAccountId(merchantID)
            .setRequestId(requestID)
            .setAmount(amount)
            .setTransactionType(transactionType)
            .setCurrency(currency)
            .setCardBundle(cardBundle)
            .build()
        cardFieldPayment.attempt3d = true
        val accountHolder = AccountHolder("John", "Doe")
        cardFieldPayment.accountHolder = accountHolder
        return optionalFieldsProvider.appendThreeDSV2Fields(cardFieldPayment) as CardFieldPayment
    }

    fun getPayPalPayment(): PayPalPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val merchantID = "bb342082-9761-4481-802e-20b10d92545d"
        val secretKey = "fd1d35aa-952e-4549-9f3d-ea33c89c86c4"
        val requestID = UUID.randomUUID().toString()
        val transactionType: TransactionType = TransactionType.DEBIT
        val amount = BigDecimal(5)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.value, amount, currency, secretKey)
        val payPalPayment: PayPalPayment = PayPalPayment.Builder()
            .setSignature(signature)
            .setMerchantAccountId(merchantID)
            .setRequestId(requestID)
            .setAmount(amount)
            .setTransactionType(transactionType)
            .setCurrency(currency)
            .setPeriodic(null)
            .setRiskReferenceId(null)
            .build()
        val notifications = Notifications()
        val list = ArrayList<Notification>()
        val notification = Notification()
        notification.url = URL_EE_TEST + "/engine/mobile/v2/notify"
        list.add(notification)
        notifications.notifications = list
        notifications.format = Notifications.FORMAT_XML
        payPalPayment.notifications = notifications
        return payPalPayment
    }

    fun getAlipayPayment(): AlipayPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "ALIPAY_MAID"
        val secretKey = "ALIPAY_SECRET"
        val transactionType: TransactionType = TransactionType.DEBIT
        val amount = BigDecimal(5)
        val currency = "USD"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return AlipayPayment.Builder()
            .setSignature(signature)
            .setMerchantAccountId(merchantId)
            .setRequestId(requestId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setLocale("EN")
            .build()
    }

    fun getWiretransferPayment(): WiretransferPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "9e035f8f-bf8d-4179-aafb-ca059128e089"
        val secretKey = "c3b39f09-5632-42fc-b1a6-2a1b386613dd"
        val transactionType: TransactionType = TransactionType.AUTHORIZATION
        val amount = BigDecimal(5)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        val orderItems: ArrayList<OrderItem> = ArrayList<OrderItem>()
        val orderItem = OrderItem()
        orderItem.quantity = 1
        orderItem.name = "Test item"
        orderItem.type = OrderItemType.SHIPMENT_FEE
        orderItem.amount = RequestedAmount(amount, currency)
        orderItems.add(orderItem)
        return WiretransferPayment.Builder()
            .setSignature(signature)
            .setMerchantAccountId(merchantId)
            .setRequestId(requestId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(AccountHolder(null, "Test"))
            .build()
    }

    fun getP24Payment(): P24Payment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "39525cf9-fe90-4f99-9ce2-42815754d7c1"
        val secretKey = "6951db91-063a-4f28-b88e-02bde3ae555e"
        val transactionType: TransactionType = TransactionType.DEBIT
        val amount = BigDecimal(1)
        val currency = "PLN"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        val orderItems: ArrayList<OrderItem> = ArrayList<OrderItem>()
        val orderItem = OrderItem()
        orderItem.quantity = 1
        orderItem.name = "Test item"
        orderItem.type = OrderItemType.SHIPMENT_FEE
        orderItem.amount = RequestedAmount(amount, currency)
        orderItems.add(orderItem)
        return P24Payment.Builder()
            .setSignature(signature)
            .setMerchantAccountId(merchantId)
            .setRequestId(requestId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(
                AccountHolder(
                    null,
                    "Test",
                    "john@doe.test"
                )
            )
            .build()
    }

    fun getSofortPayment(): SofortPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "cd553bc0-e8f1-4d4c-8a36-eb9b2040304f"
        val secretKey = "d069455a-9852-4494-95d1-5caedbdcf6ce"
        val transactionType: TransactionType = TransactionType.DEBIT
        val amount = BigDecimal(10)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return SofortPayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(
                AccountHolder(
                    "Getnetbuyer", "Spintzyk", "sofort.buyer2@getneteurope.com"
                )
            )
            .setOrderNumber("18040037092713285")
            .setDescriptor("DESC180099798888")
            .build()
    }

    fun getRatepayInvoicePayment(): RatepayInvoicePayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "1da76824-e28a-4b64-8f41-c74a0a0e0c0c"
        val secretKey = "fb9d2b5d-e89b-427b-be66-2c4704378908"
        val transactionType: TransactionType = TransactionType.AUTHORIZATION
        val amount = BigDecimal(15)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return RatepayInvoicePayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(ratePayAccountHolder)
            .setOrderNumber(UUID.randomUUID().toString())
            .setOrderItems(ratePayOrderItems)
            .setDeviceFingerprint("DeviceIdentToken")
            .build()
    }

    fun getRatepayElvPayment(): RatepayElvPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "5289ba2d-6094-4f7d-b6c2-346d1af638eb"
        val secretKey = "bb3ec883-9c5b-4e70-beff-c3151b17ecf4"
        val transactionType: TransactionType = TransactionType.AUTHORIZATION
        val amount = BigDecimal(15)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        val bankAccount: BankAccount = BankAccount.ibanBic(
            "Danske Bank Hamburg",
            "DE83203205004989123456",
            "DABADEHHXXX"
        )
        return RatepayElvPayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(ratePayAccountHolder)
            .setShippingAddress(ratePayShipping)
            .setOrderNumber(UUID.randomUUID().toString())
            .setOrderItems(ratePayOrderItems)
            .setBankAccount(bankAccount)
            .setDeviceFingerprint("DeviceIdentToken")
            .setCreditorId("DE12345667787")
            .setMandateId("25874659562")
            .build()
    }

    private val ratePayAccountHolder: AccountHolder
        private get() {
            val ratePayAccountHolder = AccountHolder()
            val address = Address()
            address.street1 = "Nicht Versenden Str. 5"
            address.city = "Testhausen"
            address.state = "Berlin"
            address.country = "DE"
            address.postalCode = "13353"
            ratePayAccountHolder.firstName = "Test"
            ratePayAccountHolder.lastName = "Wischnewski"
            ratePayAccountHolder.email = "test@test.com"
            ratePayAccountHolder.gender = "Male"
            ratePayAccountHolder.phone = "03018425165"
            ratePayAccountHolder.address = address
            return ratePayAccountHolder
        }
    private val ratePayShipping: Shipping
        private get() {
            val shipping = Shipping()
            val address = Address()
            address.street1 = "Nicht Versenden Str. 5"
            address.city = "Testhausen"
            address.state = "Berlin"
            address.country = "DE"
            address.postalCode = "13353"
            shipping.firstName = "Hans-Jürgen"
            shipping.lastName = "Wischnewski"
            shipping.phone = "+49123123123"
            shipping.address = address
            return shipping
        }
    private val ratePayOrderItems: ArrayList<OrderItem>
        private get() {
            val orderItem1 = OrderItem()
            orderItem1.name = "Item A"
            orderItem1.articleNumber = "123"
            orderItem1.amount = RequestedAmount("5.0", "EUR")
            orderItem1.taxRate = BigDecimal("0.0")
            orderItem1.quantity = 1
            val orderItem2 = OrderItem()
            orderItem2.name = "Item B"
            orderItem2.articleNumber = "456"
            orderItem2.amount = RequestedAmount("10.0", "EUR")
            orderItem2.taxRate = BigDecimal("0.0")
            orderItem2.quantity = 1
            val orderItem3 = OrderItem()
            orderItem3.name = "Shipping Cost"
            orderItem3.articleNumber = "789"
            orderItem3.amount = RequestedAmount("5.0", "EUR")
            orderItem3.taxRate = BigDecimal("0.0")
            orderItem3.quantity = 1
            orderItem3.type = OrderItemType.SHIPMENT_FEE
            val orderItem4 = OrderItem()
            orderItem4.name = "Discount"
            orderItem4.articleNumber = "111"
            orderItem4.amount = RequestedAmount("-5.0", "EUR")
            orderItem4.taxRate = BigDecimal("0.0")
            orderItem4.quantity = 1
            orderItem4.type = OrderItemType.DISCOUNT
            val items: ArrayList<OrderItem> = ArrayList<OrderItem>()
            items.add(orderItem1)
            items.add(orderItem2)
            items.add(orderItem3)
            items.add(orderItem4)
            return items
        }

    private val ziniaOrderItems: List<OrderItem>
        private get() {
            val orderItem1 = OrderItem()
            orderItem1.name = "Item A"
            orderItem1.articleNumber = "123"
            orderItem1.amount = RequestedAmount("150.0", "EUR")
            orderItem1.taxRate = BigDecimal("0.0")
            orderItem1.quantity = 1
            return listOf(orderItem1)
        }

    private val blikAccountHolder: AccountHolder
        private get() {
            val ratePayAccountHolder = AccountHolder()
            ratePayAccountHolder.email = "test@test.com"
            return ratePayAccountHolder
        }

    fun getBlikRedirectPayment(): BlikRedirectPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "54c06d30-73a7-45b0-b1f6-6f5ae91cd40c"
        val secretKey = "285fe5af-09f0-4e99-b8a8-439f14d75e20"
        val transactionType: TransactionType = TransactionType.DEBIT
        val amount = BigDecimal(15)
        val currency = "PLN"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return BlikRedirectPayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(blikAccountHolder)
            .setOrderNumber("18040037092713285")
            .setDescriptor("DESC180099798888")
            .build()
    }

    fun getBlikLevel0Payment(): BlikLevel0Payment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "54c06d30-73a7-45b0-b1f6-6f5ae91cd40c"
        val secretKey = "285fe5af-09f0-4e99-b8a8-439f14d75e20"
        val transactionType: TransactionType = TransactionType.DEBIT
        val amount = BigDecimal(15)
        val currency = "PLN"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return BlikLevel0Payment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(blikAccountHolder)
            .setOrderNumber("18040037092713285")
            .setDescriptor("DESC180099798888")
            .build()
    }

    private val bizumAccountHolder: AccountHolder
        private get() {
            val ratePayAccountHolder = AccountHolder()
            ratePayAccountHolder.phone = "+34700000000"
            return ratePayAccountHolder
        }

    fun getBizumPayment(): BizumPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "8be5c529-d148-435a-a713-ea0e7a8c4a13"
        val secretKey = "dc2ab378-9954-4254-ab3b-f0a16af74992"
        val transactionType: TransactionType = TransactionType.AUTHORIZATION
        val amount = BigDecimal(15)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return BizumPayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(bizumAccountHolder)
            .setOrderNumber("18040037092713285")
            .setDescriptor("DESC180099798888")
            .build()
    }

    private val mbWayAccountHolder = AccountHolder().apply {
//        phoneCountryCode = "+351"
//        phone = "913951034" // if provided, will skip UI w/ phone number obtaining
    }

    fun getMbWayPayment(): MbwayPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "5531b677-f707-433d-a907-3cf4acbec1e4"
        val secretKey = "af145126-5ced-4b82-aa6b-cec488446475"
        val transactionType: TransactionType = TransactionType.AUTHORIZATION
        val amount = BigDecimal(1)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return MbwayPayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(mbWayAccountHolder)
            .build()
    }

    fun getIdealPayment(): IdealPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "76977f95-d929-4870-acee-4ef89fd200cd"
        val secretKey = "01ab26af-d5c7-46f8-b5dd-8a1516c3e85c"
        val transactionType: TransactionType = TransactionType.DEBIT
        val amount = BigDecimal(1)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return IdealPayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .build()
    }

    fun getMbReferenciaPayment(): MbreferenciaPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "21d4984b-f192-4de0-a13b-6bd109a89a95"
        val secretKey = "e957a37a-6191-4c66-a9e2-53d918b206a4"
        val transactionType: TransactionType = TransactionType.PENDING_DEBIT
        val amount = BigDecimal(1)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return MbreferenciaPayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setDescriptor("descriptor")
            .build()
    }

    fun getZiniaPayment(): ZiniaPayment {
        val timestamp: String = SignatureHelper.generateTimestamp()
        val requestId = UUID.randomUUID().toString()
        val merchantId = "da194107-986a-481a-8d4d-b2cbf0f54ee4"
        val secretKey = "e2aa6601-dd1a-4550-a386-dba4521f9f3e"
        val transactionType: TransactionType = TransactionType.AUTHORIZATION
        val amount = BigDecimal(150)
        val currency = "EUR"
        val signature: String =
            SignatureHelper.generateSignature(timestamp, merchantId, requestId, transactionType.value, amount, currency, secretKey)
        return ZiniaPayment.Builder()
            .setSignature(signature)
            .setRequestId(requestId)
            .setMerchantAccountId(merchantId)
            .setTransactionType(transactionType)
            .setAmount(amount)
            .setCurrency(currency)
            .setAccountHolder(AccountHolder().apply {
                firstName = "Santos"
                lastName = "Jhonatan"
                email = "Santos.Jhonatan@gmail.com"
                address = Address().apply {
                    street1 = "Kirk Side"
                    houseNumber = "25"
                    city = "London"
                    postalCode = "qB ni"
                }
            })
            .setOrderNumber(UUID.randomUUID().toString())
            .setOrderItems(ziniaOrderItems)
            .build()
    }
}
