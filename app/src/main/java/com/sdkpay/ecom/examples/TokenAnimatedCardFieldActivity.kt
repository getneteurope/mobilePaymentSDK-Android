/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */
package com.sdkpay.ecom.examples

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdkpay.ecom.Client
import com.sdkpay.ecom.card.AnimatedCardFieldFragment
import com.sdkpay.ecom.card.model.CardBundle
import com.sdkpay.ecom.card.model.CardFieldPayment
import com.sdkpay.ecom.card.ui.cardform.animated.AnimatedCardFieldState
import com.sdkpay.ecom.cardbrand.CardBrand
import com.sdkpay.ecom.examples.Constants.REQUEST_TIMEOUT
import com.sdkpay.ecom.examples.Constants.URL_EE_TEST
import com.sdkpay.ecom.model.AccountHolder
import com.sdkpay.ecom.model.CardToken
import com.sdkpay.ecom.model.PaymentResponse
import com.sdkpay.ecom.model.TransactionType
import com.sdkpay.ecom.util.Observer
import io.reactivex.functions.Consumer
import java.math.BigDecimal
import java.util.UUID

class TokenAnimatedCardFieldActivity : AppCompatActivity(), Observer<PaymentResponse> {
    private val mContext: Context = this
    lateinit var animatedCardFieldFragment: AnimatedCardFieldFragment
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated_card_form)
        animatedCardFieldFragment = AnimatedCardFieldFragment.Builder()
            .setRequestFocus(true)
            .setRequireManualCardBrandSelection(true)
            .setToken("5500971586101006")
            .setCardBrand(CardBrand.VISA)
            .build()
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.card_field_container, animatedCardFieldFragment)
            .commit()
        animatedCardFieldFragment
            .getEventObserver()
            .subscribe(
                Consumer<AnimatedCardFieldState> { state: AnimatedCardFieldState -> Log.i("event", state.toString()) }
            )
    }

    fun onSubmitButtonClicked(view: View?) {
        val bundle = animatedCardFieldFragment.getCardBundle()
        if (bundle != null) {
            Client(this, URL_EE_TEST, REQUEST_TIMEOUT).startPayment(getCardFormPayment(bundle))
            findViewById<View>(R.id.progress).setVisibility(View.VISIBLE)
        } else {
            Toast.makeText(mContext, "Card bundle is null!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCardFormPayment(cardBundle: CardBundle?): CardFieldPayment {
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
        runOnUiThread(
            Runnable {
                Toast.makeText(this, ResponseHelper.getFormattedResponse(paymentResponse), Toast.LENGTH_SHORT).show()
                findViewById<View>(R.id.progress).setVisibility(View.GONE)
            }
        )
    }
}
