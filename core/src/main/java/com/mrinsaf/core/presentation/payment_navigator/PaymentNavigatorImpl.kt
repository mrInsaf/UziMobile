package com.mrinsaf.core.presentation.payment_navigator

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PaymentNavigatorImpl @Inject constructor(
    @ApplicationContext private val context: Context
): PaymentNavigator {
    override fun openPaymentUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }

}