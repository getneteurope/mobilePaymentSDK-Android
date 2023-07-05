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
import com.sdkpay.ecom.card.ui.cardform.animated.AnimatedCardFieldState
import com.sdkpay.ecom.examples.Constants.REQUEST_TIMEOUT
import com.sdkpay.ecom.examples.Constants.URL_EE_TEST
import com.sdkpay.ecom.examples.ResponseHelper.getFormattedResponse
import com.sdkpay.ecom.examples.providers.OptionalFieldsProvider
import com.sdkpay.ecom.examples.providers.PaymentObjectProvider
import com.sdkpay.ecom.model.PaymentResponse
import com.sdkpay.ecom.util.Observer

class AnimatedCardFieldActivity : AppCompatActivity(), Observer<PaymentResponse> {
    private val mContext: Context = this
    lateinit var animatedCardFieldFragment: AnimatedCardFieldFragment
    var mPaymentObjectProvider = PaymentObjectProvider(OptionalFieldsProvider())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animated_card_form)
        animatedCardFieldFragment = AnimatedCardFieldFragment.Builder()
            .setRequestFocus(true)
            .setRequireManualCardBrandSelection(true)
            .build()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.card_field_container, animatedCardFieldFragment)
            .commit()
        animatedCardFieldFragment
            .getEventObserver()
            .subscribe { state: AnimatedCardFieldState -> Log.i("event", state.toString()) }
    }

    fun onSubmitButtonClicked(view: View?) {
        val bundle = animatedCardFieldFragment.getCardBundle()
        if (bundle != null) {
            Client(this, URL_EE_TEST, REQUEST_TIMEOUT).startPayment(
                mPaymentObjectProvider.getCardFormPayment(bundle)
            )
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
