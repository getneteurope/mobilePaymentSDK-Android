/*
 * Copyright © 2022 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U. All rights reserved.
 */
package com.sdkpay.ecom.examples

import android.util.Base64
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object SignatureHelper {
    private const val ENCRYPTION_ALGORITHM = "HS256"
    private const val UTF_8 = "UTF-8"
    fun generateTimestamp(): String {
        val timeZone = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance(timeZone)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.format(calendar.time)
    }

    /**
     * Sample for generating signature according to input
     * Note that signature should be generated server side and should not be generated in the app because of security matters
     */
    fun generateSignature(
        timestamp: String, merchantID: String, requestID: String,
        transactionType: String, amount: BigDecimal?, currency: String?, secretKey: String
    ): String {
        var payload = """
             ${ENCRYPTION_ALGORITHM.uppercase(Locale.getDefault())}
             request_time_stamp=$timestamp
             merchant_account_id=$merchantID
             request_id=$requestID
             transaction_type=$transactionType
             """.trimIndent()
        if (amount != null && currency != null) {
            payload += """

                requested_amount=$amount
                requested_amount_currency=
                """.trimIndent() + currency.uppercase(Locale.getDefault())
        }
        try {
            val encryptedPayload = encryptSignature(payload, secretKey)
            return (String(Base64.encode(payload.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP), StandardCharsets.UTF_8)
                    + "." + String(Base64.encode(encryptedPayload, Base64.NO_WRAP), StandardCharsets.UTF_8))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw RuntimeException("generateSignature error")
    }

    private fun encryptSignature(payload: String, secretKey: String): ByteArray {
        try {
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(SecretKeySpec(secretKey.toByteArray(), "HmacSHA256"))
            return mac.doFinal(payload.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ByteArray(1)
    }
}
