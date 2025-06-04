package com.mrinsaf.profile.data.mapper

import com.mrinsaf.core.data.network.dto.network_responses.TariffPlanResponse
import com.mrinsaf.profile.domain.model.TariffPlan

fun TariffPlanResponse.toTariffPlan(): TariffPlan {
    return TariffPlan(
        name = this.name,
        description = this.description,
        price = this.price
    )
}