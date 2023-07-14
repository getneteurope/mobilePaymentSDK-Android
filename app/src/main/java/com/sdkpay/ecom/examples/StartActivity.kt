/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */
package com.sdkpay.ecom.examples

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sdkpay.ecom.examples.BuildConfig
import com.sdkpay.ecom.Client
import com.sdkpay.ecom.Client.Companion.EXTRA_PAYMENT_SDK_RESPONSE
import com.sdkpay.ecom.Client.Companion.PAYMENT_SDK_REQUEST_CODE
import com.sdkpay.ecom.examples.Constants.URL_EE_TEST
import com.sdkpay.ecom.examples.providers.OptionalFieldsProvider
import com.sdkpay.ecom.examples.providers.PaymentObjectProvider
import com.sdkpay.ecom.model.BasePayment
import com.sdkpay.ecom.model.PaymentResponse
import java.io.Serializable

class StartActivity : AppCompatActivity() {
    private val mPaymentObjectProvider: PaymentObjectProvider = PaymentObjectProvider(OptionalFieldsProvider())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val payments = mapOf<String, () -> Unit>(
            getString(R.string.start_simple_card_payment) to { startPayment { mPaymentObjectProvider.getCardPayment(false) } },
            getString(R.string.start_animated_card_payment) to { startPayment { mPaymentObjectProvider.getCardPayment(true) } },
            getString(R.string.start_simple_card_token_payment) to { startPayment { mPaymentObjectProvider.getCardTokenPayment() } },
            getString(R.string.start_card_field_payment) to { startActivity(Intent(this, CardFieldActivity::class.java)) },
            getString(R.string.start_optional_parameters_example) to { startPayment { mPaymentObjectProvider.getCardPaymentWithOptionalData() } },
            getString(R.string.start_animated_card_field_payment) to { startActivity(Intent(this, AnimatedCardFieldActivity::class.java)) },
            getString(R.string.start_token_animated_card_field_payment) to { startActivity(Intent(this, TokenAnimatedCardFieldActivity::class.java)) },
            getString(R.string.start_sepa_payment) to { startPayment { mPaymentObjectProvider.getSepaPaymentObject() } },
            getString(R.string.start_paypal_payment) to { startPayment { mPaymentObjectProvider.getAlipayPayment() } },
            getString(R.string.start_alipay_payment) to { startPayment { mPaymentObjectProvider.getWiretransferPayment() } },
            getString(R.string.start_wiretransfer_payment) to { startPayment { mPaymentObjectProvider.getP24Payment() } },
            getString(R.string.start_p24_payment) to { startPayment { mPaymentObjectProvider.getSofortPayment() } },
            getString(R.string.start_ratepay_invoice_payment) to { startPayment { mPaymentObjectProvider.getRatepayInvoicePayment() } },
            getString(R.string.start_ratepay_elv_payment) to { startPayment { mPaymentObjectProvider.getRatepayElvPayment() } },
            getString(R.string.start_blik_redirect_payment) to { startPayment { mPaymentObjectProvider.getBlikRedirectPayment() } },
            getString(R.string.start_blik_level0_payment) to { startPayment { mPaymentObjectProvider.getBlikLevel0Payment() } },
            getString(R.string.start_bizum_payment) to { startPayment { mPaymentObjectProvider.getBizumPayment() } },
            getString(R.string.start_mbway_payment) to { startPayment { mPaymentObjectProvider.getMbWayPayment() } },
            getString(R.string.start_ideal_payment) to { startPayment { mPaymentObjectProvider.getIdealPayment() } },
            getString(R.string.start_mbreferencia_payment) to { startPayment { mPaymentObjectProvider.getMbReferenciaPayment() } },
            getString(R.string.start_zinia_payment) to { startPayment { mPaymentObjectProvider.getZiniaPayment() } },
        )
        setContent {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
                payments.forEach {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = { it.value() }) {
                        Text(it.key)
                    }
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = """
                App version: ${BuildConfig.VERSION_NAME}_${if (BuildConfig.DEBUG) "debug" else "release"}
                SDK version: ${com.sdkpay.ecom.BuildConfig.VERSION_NAME}_${if (com.sdkpay.ecom.BuildConfig.DEBUG) "debug" else "release"}
                """.trimIndent()
                )
            }
        }
    }


    private fun startPayment(env: String = URL_EE_TEST, payment: () -> BasePayment) {
        try {
            Client(this, env)
                .startPayment(payment())
        } catch (e: Exception) {
            Toast.makeText(this, "Error starting payment", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT_SDK_REQUEST_CODE) {
            val paymentSdkResponse: Serializable? = data?.getSerializableExtra(EXTRA_PAYMENT_SDK_RESPONSE)
            if (paymentSdkResponse is PaymentResponse) {
                val formattedResponse: String = ResponseHelper.getFormattedResponse(paymentSdkResponse as PaymentResponse)
                Toast.makeText(this, formattedResponse, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
