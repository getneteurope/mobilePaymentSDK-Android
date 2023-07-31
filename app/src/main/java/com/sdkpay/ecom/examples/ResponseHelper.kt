/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
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
