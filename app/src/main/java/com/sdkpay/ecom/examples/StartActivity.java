/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */

package com.sdkpay.ecom.examples;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sdkpay.ecom.Client;
import com.sdkpay.ecom.examples.providers.OptionalFieldsProvider;
import com.sdkpay.ecom.examples.providers.PaymentObjectProvider;
import com.sdkpay.ecom.model.PaymentResponse;

import java.io.Serializable;

import static com.sdkpay.ecom.examples.Constants.REQUEST_TIMEOUT;
import static com.sdkpay.ecom.examples.Constants.URL_EE_TEST;

public class StartActivity extends AppCompatActivity {
    private final Context mContext = this;
    private final PaymentObjectProvider mPaymentObjectProvider = new PaymentObjectProvider(new OptionalFieldsProvider());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void makeSepaPayment(View view) {
        new Client(mContext, URL_EE_TEST,  REQUEST_TIMEOUT)
                .startPayment(mPaymentObjectProvider.getSepaPaymentObject());
    }

    public void makeSimpleCardPayment(View view) {
        new Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.getCardPayment(false));
    }

    public void makeSimpleCardTokenPayment(View view) {
        new Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.getCardTokenPayment());
    }

    public void makeAnimatedCardPayment(View view) {
        new Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.getCardPayment(true));
    }

    public void makeCardFieldPayment(View view) {
        startActivity(new Intent(mContext, CardFieldActivity.class));
    }

    public void makeAnimatedCardFieldPayment(View view) {
        startActivity(new Intent(mContext, AnimatedCardFieldActivity.class));
    }

    public void makeTokenAnimatedCardFieldPayment(View view){
        startActivity(new Intent(mContext, TokenAnimatedCardFieldActivity.class));
    }

    public void makeFragmentCardFieldPayment(View view){
        startActivity(new Intent(mContext, KotlinCardFieldFragmentImplActivity.class));
    }

    public void makeCardPaymentWithOptionalParameters(View view){
        new Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.getCardPaymentWithOptionalData());
    }

    public void makePaypalPayment(View view) {
        // Do not forget to add sdkpay_ecom_paypal_scheme and sdkpay_ecom_paypal_host to your string file and initialise it!
        new Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.getPayPalPayment());
    }

    public void makeAlipayPayment(View view){
        new Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.getAlipayPayment());
    }

    public void makeWiretransferPayment(View view){
        new Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.getWiretransferPayment());
    }

    public void makeP24Payment(View view){
        new Client(mContext, URL_EE_TEST)
                .startPayment(mPaymentObjectProvider.getP24Payment());
    }

    public void makeLoyaltyCard(View view){
        startActivity(new Intent(mContext, KotlinLoyaltyCardFieldActivity.class));
    }

    public void kotlinDemo(View view){
        startActivity(new Intent(mContext, KotlinStartActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Client.PAYMENT_SDK_REQUEST_CODE) {
            Serializable paymentSdkResponse = data.getSerializableExtra(Client.EXTRA_PAYMENT_SDK_RESPONSE);
            if (paymentSdkResponse instanceof PaymentResponse) {
                String formattedResponse = ResponseHelper.getFormattedResponse((PaymentResponse) paymentSdkResponse);
                Toast.makeText(this, formattedResponse, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
