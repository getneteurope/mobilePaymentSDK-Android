/*
 * Copyright © 2023 PagoNxt Merchant Solutions S.L. and Santander España Merchant Services, Entidad de Pago, S.L.U.
 * You may not use this file except in compliance with the License which is available at https://mit-license.org/
 */

package com.sdkpay.ecom.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class KotlinCardFieldFragmentImplActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cardfield_fragment)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, CardFieldFragmentImplFragment())
                .commit()
    }
}
