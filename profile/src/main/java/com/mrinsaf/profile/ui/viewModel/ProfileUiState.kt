package com.mrinsaf.profile.ui.viewModel

import com.mrinsaf.core.domain.model.User
import com.mrinsaf.profile.domain.model.ActiveSubscription
import com.mrinsaf.profile.domain.model.PaymentProvider
import com.mrinsaf.profile.domain.model.TariffPlan

data class ProfileUiState(
    val user: User? = null,
    val activeSubscription: ActiveSubscription? = null,

    val tariffPlansList: List<TariffPlan> = emptyList(),
    val selectedTariffPlanId: String? = null,

    val paymentProviderList: List<PaymentProvider> = emptyList(),
    val selectedProviderId: String? = null,
)