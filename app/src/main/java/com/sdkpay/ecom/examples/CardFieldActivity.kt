/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
 */
package com.sdkpay.ecom.examples

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdkpay.ecom.Client
import com.sdkpay.ecom.card.CardFieldFragment
import com.sdkpay.ecom.card.ui.cardform.CardFieldState
import com.sdkpay.ecom.examples.Constants.REQUEST_TIMEOUT
import com.sdkpay.ecom.examples.Constants.URL_EE_TEST
import com.sdkpay.ecom.examples.ResponseHelper.getFormattedResponse
import com.sdkpay.ecom.examples.providers.OptionalFieldsProvider
import com.sdkpay.ecom.examples.providers.PaymentObjectProvider
import com.sdkpay.ecom.model.PaymentResponse
import com.sdkpay.ecom.util.Observer

class CardFieldActivity : AppCompatActivity(), Observer<PaymentResponse> {
    private val mContext: Context = this
    private val mPaymentObjectProvider = PaymentObjectProvider(OptionalFieldsProvider())
    lateinit var cardFieldFragment: CardFieldFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_form)
        cardFieldFragment = CardFieldFragment.Builder()
            .setRequireManualCardBrandSelection(true)
            .build()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.card_field_container, cardFieldFragment)
            .commit()
        cardFieldFragment
            .getEventObserver()
            .subscribe { state: CardFieldState -> Log.i("event", state.toString()) }
    }

    fun onSubmitButtonClicked(view: View?) {
        val bundle = cardFieldFragment.getCardBundle()
        if (bundle != null) {
            Client(this, URL_EE_TEST, REQUEST_TIMEOUT).startPayment(mPaymentObjectProvider.getCardFormPayment(bundle))
            findViewById<View>(R.id.progress).visibility = View.VISIBLE
        } else {
            Toast.makeText(mContext, "Card bundle is null!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onObserve(paymentResponse: PaymentResponse) {
        runOnUiThread {
            Toast.makeText(this, getFormattedResponse(paymentResponse), Toast.LENGTH_SHORT).show()
            findViewById<View>(R.id.progress).visibility = View.GONE
        }
    }
}
