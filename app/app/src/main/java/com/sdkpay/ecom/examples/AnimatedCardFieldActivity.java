/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
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
import com.sdkpay.ecom.examples.providers.OptionalFieldsProvider;
import com.sdkpay.ecom.examples.providers.PaymentObjectProvider;
import com.sdkpay.ecom.model.PaymentResponse;
import com.sdkpay.ecom.util.Observer;

import java.math.BigDecimal;
import java.util.UUID;

import static com.sdkpay.ecom.examples.Constants.REQUEST_TIMEOUT;
import static com.sdkpay.ecom.examples.Constants.URL_EE_TEST;

public class AnimatedCardFieldActivity extends AppCompatActivity implements Observer<PaymentResponse> {
    private final Context mContext = this;
    AnimatedCardFieldFragment animatedCardFieldFragment;
    PaymentObjectProvider mPaymentObjectProvider = new PaymentObjectProvider(new OptionalFieldsProvider());
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
            new Client(this, URL_EE_TEST, REQUEST_TIMEOUT).startPayment(mPaymentObjectProvider.getCardFormPayment(animatedCardFieldFragment.getCardBundle()));
            findViewById(R.id.progress).setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(mContext, "Card bundle is null!", Toast.LENGTH_SHORT).show();
        }
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
