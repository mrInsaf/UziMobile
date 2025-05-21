package com.mrinsaf.profile.ui.viewModel

import com.mrinsaf.core.domain.model.User

data class ProfileUiState(
    val user: User? = null,

    val subscriptionDaysRemaining: Int = 0,
    val activeSubscriptionName: String? = null,
)