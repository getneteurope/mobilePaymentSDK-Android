/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
 */

package com.sdkpay.ecom.examples;

import com.sdkpay.ecom.model.Status;
import com.sdkpay.ecom.model.PaymentResponse;

public class ResponseHelper {

    public static String getFormattedResponse(PaymentResponse paymentSdkResponse) {
        StringBuilder sb = new StringBuilder();
        // see com.sdkpay.ecom.ResponseCode.kt
        sb.append("Response code: ")
                .append(paymentSdkResponse.getResponseCode());
        if(paymentSdkResponse.getErrorMessage() != null){
            sb.append(paymentSdkResponse.getErrorMessage());
        }
        if(paymentSdkResponse.getPayment() != null && paymentSdkResponse.getPayment().getStatuses() != null){
            sb.append("\n");
            for (Status status: paymentSdkResponse.getPayment().getStatuses()){
                sb.append(status.getCode());
                sb.append(": ");
                sb.append(status.getDescription());
            }
        }
        return sb.toString();
    }
}
