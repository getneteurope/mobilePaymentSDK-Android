/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
 */

package com.sdkpay.ecom.examples;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sdkpay.ecom.Client;
import com.sdkpay.ecom.card.AnimatedCardFieldFragment;
import com.sdkpay.ecom.card.model.CardBundle;
import com.sdkpay.ecom.card.model.CardFieldPayment;
import com.sdkpay.ecom.model.AccountHolder;
import com.sdkpay.ecom.model.TransactionType;
import com.sdkpay.ecom.model.PaymentResponse;
import com.sdkpay.ecom.util.Observer;

import java.math.BigDecimal;
import java.util.UUID;

import static com.sdkpay.ecom.examples.Constants.REQUEST_TIMEOUT;
import static com.sdkpay.ecom.examples.Constants.URL_EE_TEST;

public class AnimatedCardFieldActivity extends AppCompatActivity implements Observer<PaymentResponse> {
    private final Context mContext = this;
    AnimatedCardFieldFragment animatedCardFieldFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_card_form);

        animatedCardFieldFragment = new AnimatedCardFieldFragment.Builder()
                .setRequestFocus(true)
                .setRequireManualCardBrandSelection(true)
                .build();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.card_field_container, animatedCardFieldFragment)
                .commit();

        animatedCardFieldFragment
                .getEventObserver()
                .subscribe(
                        state -> {
                            Log.i("event", state.toString());
                        }
                );
    }

    public void onSubmitButtonClicked(View view) {
        if(animatedCardFieldFragment.getCardBundle() != null) {
            new Client(this, URL_EE_TEST, REQUEST_TIMEOUT).startPayment(getCardFormPayment(animatedCardFieldFragment.getCardBundle()));
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(mContext, "Card bundle is null!", Toast.LENGTH_SHORT).show();
        }
    }

    public CardFieldPayment getCardFormPayment(CardBundle cardBundle) {
        String timestamp = SignatureHelper.generateTimestamp();
        String merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108";
        String secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53";
        String requestID = UUID.randomUUID().toString();
        TransactionType transactionType = TransactionType.PURCHASE;
        BigDecimal amount = new BigDecimal(5);
        String currency = "EUR";
        String signature = SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.getValue(), amount, currency, secretKey);

        CardFieldPayment cardFieldPayment = new CardFieldPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantID)
                .setRequestId(requestID)
                .setAmount(amount)
                .setTransactionType(transactionType)
                .setCurrency(currency)
                .setCardBundle(cardBundle)
                .build();

        AccountHolder accountHolder = new AccountHolder("John", "Doe");
        cardFieldPayment.setAccountHolder(accountHolder);

        return cardFieldPayment;
    }

    @Override
    public void onObserve(PaymentResponse paymentResponse) {
        runOnUiThread(() -> {
                    Toast.makeText(this, ResponseHelper.getFormattedResponse(paymentResponse), Toast.LENGTH_SHORT).show();
                    findViewById(R.id.progress).setVisibility(View.GONE);
                }

        );
    }
}
