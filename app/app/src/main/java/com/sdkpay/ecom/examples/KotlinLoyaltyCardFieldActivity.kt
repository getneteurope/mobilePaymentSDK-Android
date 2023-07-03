/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */

package com.sdkpay.ecom.examples

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sdkpay.ecom.Client
import com.sdkpay.ecom.card.CardFieldFragment
import com.sdkpay.ecom.card.model.CardBundle
import com.sdkpay.ecom.card.model.CardFieldPayment
import com.sdkpay.ecom.cardbrand.CardBrand
import com.sdkpay.ecom.examples.Constants.URL_EE_TEST
import com.sdkpay.ecom.model.*
import com.sdkpay.ecom.util.Observer
import java.util.*

class KotlinLoyaltyCardFieldActivity : AppCompatActivity(), Observer<PaymentResponse> {

    private var cardFieldFragment : CardFieldFragment? = null
    private val  mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_form)

        cardFieldFragment = CardFieldFragment.Builder()
                .setRequireManualCardBrandSelection(true)
                .setSupportedCardBrands(setOf(CardBrand.VISA, CardBrand.MASTERCARD))
                .build()

        cardFieldFragment?.let {cardFieldFragment ->
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.card_field_container, cardFieldFragment)
                    .commit()
        } ?: kotlin.run {
            Toast.makeText(mContext, "Card fragment is null!", Toast.LENGTH_LONG).show()
        }
    }

    fun onSubmitButtonClicked(view: View){
        cardFieldFragment?.getCardBundle()?.let {cardBundle ->
            Client(mContext,URL_EE_TEST, 30)
                    .startPayment(getPayment(cardBundle))
        } ?: kotlin.run {
            Toast.makeText(mContext, "Card bundle is null!", Toast.LENGTH_LONG).show()
        }
   }

    override fun onObserve(paymentResponse: PaymentResponse){
            Toast.makeText(mContext, ResponseHelper.getFormattedResponse(paymentResponse), Toast.LENGTH_SHORT).show()
    }

    private fun getPayment(cardBundle: CardBundle) :BasePayment{
        val timestamp = SignatureHelper.generateTimestamp()

        //MasterCard merchant ID and secretKey
        val merchantID = "5c4a8a42-04a8-4970-a595-262f0ba0a108"
        val secretKey = "5ac555d4-e7f7-409f-8147-d82c8c10ed53"

        val requestID = UUID.randomUUID().toString()
        val transactionType = TransactionType.AUTHORIZATION
        val signature = SignatureHelper.generateSignature(timestamp, merchantID, requestID, transactionType.value, null,null,secretKey)

        val cardFieldPayment = CardFieldPayment.Builder()
                .setSignature(signature)
                .setMerchantAccountId(merchantID)
                .setRequestId(requestID)
                .setTransactionType(transactionType)
                .setCardBundle(cardBundle)
                .build()

        cardFieldPayment.accountHolder = getAccountHolder()

        if (cardBundle.cardBrand == CardBrand.MASTERCARD.toString().toLowerCase()){
            cardFieldPayment.loyaltyCard = getLoyaltyCard()
        }

        return cardFieldPayment
    }

    private fun getAccountHolder() : AccountHolder{
        return AccountHolder().apply {
            email = "john.doe${(1..10000000).random()}@gmail.com"
        }
    }

    private fun getLoyaltyCard() : LoyaltyCard{
        return LoyaltyCard().apply {
            userId = Math.random().toString()
            promotionCode = "SHOPBACKVN"
            productCode = "MCCSHOPBACK"
        }
    }

}
