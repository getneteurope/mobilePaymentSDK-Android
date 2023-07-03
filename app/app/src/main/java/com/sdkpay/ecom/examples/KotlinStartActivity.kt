/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */

package com.sdkpay.ecom.examples

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdkpay.ecom.Client
import com.sdkpay.ecom.examples.Constants.REQUEST_TIMEOUT
import com.sdkpay.ecom.examples.Constants.URL_EE_TEST
import com.sdkpay.ecom.examples.providers.OptionalFieldsProvider
import com.sdkpay.ecom.examples.providers.PaymentObjectProvider
import com.sdkpay.ecom.model.PaymentResponse

class KotlinStartActivity : AppCompatActivity() {
    private val mContext = this
    private val mPaymentObjectProvider = PaymentObjectProvider(OptionalFieldsProvider())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        findViewById<Button>(R.id.button_kotlin_example).visibility = View.GONE
    }

    fun makeSepaPayment(view: View) {
        Client(mContext, URL_EE_TEST, REQUEST_TIMEOUT)
                .startPayment(mPaymentObjectProvider.sepaPaymentObject)
    }

    fun makeSimpleCardPayment(view: View) {
        Client(mContext, URL_EE_TEST,  REQUEST_TIMEOUT)
                .startPayment(mPaymentObjectProvider.getCardPayment(false))
    }

    fun makeFragmentCardFieldPayment(view: View?) {
        startActivity(Intent(mContext, KotlinCardFieldFragmentImplActivity::class.java))
    }

    fun makeCardPaymentWithOptionalParameters(view: View) {
        Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.cardPaymentWithOptionalData)
    }

    fun makeSimpleCardTokenPayment(view: View) {
        Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.cardTokenPayment)
    }

    fun makeAnimatedCardPayment(view: View) {
        Client(mContext, URL_EE_TEST, REQUEST_TIMEOUT)
                .startPayment(mPaymentObjectProvider.getCardPayment(true))
    }

    fun makeAnimatedCardFieldPayment(view: View) {
        startActivity(Intent(mContext, KotlinAnimatedCardFieldActivity::class.java))
    }

    fun makeTokenAnimatedCardFieldPayment(view: View){
        startActivity(Intent(mContext, KotlinTokenAnimatedCardFieldActivity::class.java))
    }

    fun makeCardFieldPayment(view: View) {
        startActivity(Intent(mContext, KotlinCardFieldActivity::class.java))
    }

    fun makeLoyaltyCard(view: View?) {
        startActivity(Intent(mContext, KotlinLoyaltyCardFieldActivity::class.java))
    }

    fun makePaypalPayment(view: View) {
        // Do not forget to add sdkpay_ecom_paypal_scheme and sdkpay_ecom_paypal_host to your string file and initialise it!
        Client(mContext, URL_EE_TEST, REQUEST_TIMEOUT)
                .startPayment(mPaymentObjectProvider.payPalPayment)
    }

    fun makeAlipayPayment(view: View?) {
        Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.alipayPayment)
    }

    fun makeWiretransferPayment(view: View?) {
        Client(mContext, URL_EE_TEST)
            .startPayment(mPaymentObjectProvider.wiretransferPayment)
    }

    fun makeP24Payment(view: View?) {
        Client(mContext, URL_EE_TEST)
            .startPayment(mPaymentObjectProvider.p24Payment)
    }

    fun makeSofortPayment(view: View?) {
            Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.sofortPayment)
    }

    fun makeRatepayInvoicePayment(view: View?) {
            Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.ratepayInvoicePayment)
    }

    fun makeRatepayElvPayment(view: View?) {
       Client(mContext, URL_EE_TEST)
           .startPayment(mPaymentObjectProvider.ratepayElvPayment)
    }

    fun makeBlikRedirectPayment(view: View?) {
        Client(mContext, URL_EE_TEST)
            .startPayment(mPaymentObjectProvider.blikRedirectPayment)
    }

    fun makeBlikLevel0Payment(view: View?) {
        Client(mContext, URL_EE_TEST)
            .startPayment(mPaymentObjectProvider.blikLevel0Payment)
    }

    fun makeBizumPayment(view: View?) {
        Client(mContext, URL_EE_TEST)
            .startPayment(mPaymentObjectProvider.bizumPayment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data ?: return

        if (requestCode == Client.PAYMENT_SDK_REQUEST_CODE) {
            val paymentSdkResponse = data.getSerializableExtra(Client.EXTRA_PAYMENT_SDK_RESPONSE)
            if (paymentSdkResponse is PaymentResponse) {
                val formattedResponse = ResponseHelper.getFormattedResponse(paymentSdkResponse)
                Toast.makeText(this, formattedResponse, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
