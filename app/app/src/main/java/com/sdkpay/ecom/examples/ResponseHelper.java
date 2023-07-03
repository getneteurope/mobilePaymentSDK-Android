/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
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
