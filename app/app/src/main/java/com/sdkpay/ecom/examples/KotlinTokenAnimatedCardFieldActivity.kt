/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */

package com.sdkpay.ecom.examples

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdkpay.ecom.Client
import com.sdkpay.ecom.card.AnimatedCardFieldFragment
import com.sdkpay.ecom.card.model.CardBundle
import com.sdkpay.ecom.card.model.CardFieldPayment
import com.sdkpay.ecom.cardbrand.CardBrand
import com.sdkpay.ecom.examples.Constants.REQUEST_TIMEOUT
import com.sdkpay.ecom.examples.Constants.URL_EE_TEST
import com.sdkpay.ecom.model.AccountHolder
import com.sdkpay.ecom.model.CardToken
import com.sdkpay.ecom.model.PaymentResponse
import com.sdkpay.ecom.model.TransactionType
import com.sdkpay.ecom.util.Observer
import java.math.BigDecimal
import java.util.*

class KotlinTokenAnimatedCardFieldActivity : AppCompatActivity(), Observer<PaymentResponse> {
    private val mContext = this
    private lateinit var animatedCardFieldFragment: AnimatedCardFieldFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated_card_form)

        animatedCardFieldFragment = AnimatedCardFieldFragment.Builder()
                .setRequestFocus(true)
                .setRequireManualCardBrandSelection(true)
                .setToken("4304509873471003")
                .setCardBrand(CardBrand.VISA)
                .build()

        supportFragmentManager
                .beginTransaction()
                .add(R.id.card_field_container, animatedCardFieldFragment)
                .commit()

        animatedCardFieldFragment
                .getEventObserver()
                .subscribe { state -> Log.i("event", state.toString()) }
    }

    fun onSubmitButtonClicked(view: View) {
        if (animatedCardFieldFragment.getCardBundle() != null) {
            Client(this, URL_EE_TEST, REQUEST_TIMEOUT).startPayment(getCardFormPayment(animatedCardFieldFragment.getCardBundle()))
            findViewById<View>(R.id.progress).visibility = View.VISIBLE
        } else {
            Toast.makeText(mContext, "Card bundle is null!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCardFormPayment(cardBundle: CardBundle?): CardFieldPayment {
        val timestamp = SignatureHelper.generateTimestamp()
        val merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108"
        val secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53"
        val requestID = UUID.randomUUID().toString()
        val transactionType = TransactionType.PURCHASE
        val amount = BigDecimal(5)
        val currency = "EUR"
        val signature = SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.value, amount, currency, secretKey)

        val cardFieldPayment = CardFieldPayment.Builder()
                .setSignature(signature!!)
                .setMerchantAccountId(merchantID)
                .setRequestId(requestID)
                .setAmount(amount)
                .setTransactionType(transactionType)
                .setCurrency(currency)
                .setCardBundle(cardBundle!!)
                .build()

        val accountHolder = AccountHolder("John", "Doe")
        cardFieldPayment.accountHolder = accountHolder

        val cardToken = CardToken()
        cardToken.tokenId = "5427317855871006"
        cardToken.maskedAccountNumber = "541333******1006"
        cardFieldPayment.cardToken = cardToken

        return cardFieldPayment
    }

    override fun onObserve(paymentResponse: PaymentResponse) {
        runOnUiThread {
            Toast.makeText(this, ResponseHelper.getFormattedResponse(paymentResponse), Toast.LENGTH_SHORT).show()
            findViewById<View>(R.id.progress).visibility = View.GONE
        }
    }
}
