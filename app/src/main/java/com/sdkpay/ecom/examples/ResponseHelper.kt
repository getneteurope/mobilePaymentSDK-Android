/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */
package com.sdkpay.ecom.examples

import com.sdkpay.ecom.model.PaymentResponse

object ResponseHelper {
    fun getFormattedResponse(paymentSdkResponse: PaymentResponse): String {
        val sb = StringBuilder()
        // see com.sdkpay.ecom.ResponseCode.kt
        sb.append("Response code: ")
            .append(paymentSdkResponse.responseCode)
        if (paymentSdkResponse.errorMessage != null) {
            sb.append(paymentSdkResponse.errorMessage)
        }
        if (paymentSdkResponse.payment != null && paymentSdkResponse.payment!!.statuses != null) {
            sb.append("\n")
            for (status in paymentSdkResponse.payment!!.statuses!!) {
                sb.append(status.code)
                sb.append(": ")
                sb.append(status.description)
            }
        }
        return sb.toString()
    }
}
