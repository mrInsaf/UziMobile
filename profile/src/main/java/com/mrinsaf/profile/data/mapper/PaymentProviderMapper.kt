package com.mrinsaf.profile.data.mapper

import com.mrinsaf.core.data.network.dto.network_responses.PaymentProviderResponse
import com.mrinsaf.profile.domain.model.PaymentProvider

fun PaymentProviderResponse.toPaymentProvider() =
    PaymentProvider(
        id = this.id,
        name = this.name,
        description = null,
        isActive = this.isActive,
    )