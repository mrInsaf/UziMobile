package com.mrinsaf.core.presentation.payment_navigator

import android.content.Context

interface PaymentNavigator {
    fun openPaymentUrl(context: Context, url: String)
}