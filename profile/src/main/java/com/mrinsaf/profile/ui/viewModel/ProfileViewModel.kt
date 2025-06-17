package com.mrinsaf.profile.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.local.data_source.UserInfoStorage
import com.mrinsaf.core.domain.model.api_result.ApiResult
import com.mrinsaf.core.domain.repository.SubscriptionRepository
import com.mrinsaf.core.domain.repository.UziServiceRepository
import com.mrinsaf.profile.data.mapper.toTariffPlan
import com.mrinsaf.profile.domain.model.ActiveSubscription
import com.mrinsaf.profile.domain.use_case.GetActiveSubscriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val uziServiceRepository: UziServiceRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val getActiveSubscription: GetActiveSubscriptionUseCase,
    @ApplicationContext val context: Context
): ViewModel() {
    private var _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState>
        get() = _uiState

    suspend fun loadUserInfo() {
        println("Достаю информацию о пользователе")
        try {
            val userId = UserInfoStorage.getUserId(context).first()
            val userInfo = uziServiceRepository.getPatient(userId)
            println(userInfo)

            _uiState.update { it.copy(userInfo) }
        }
        catch (e: Exception) {
            println("Ошибка при получении информации о пользователе $e")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchSubscriptionInfo() = viewModelScope.launch {
        when (val result = getActiveSubscription()) {
            is ApiResult.Success -> {
                updateActiveSubscriptionState(result.data)
            }
            is ApiResult.Error -> {
                updateActiveSubscriptionState(null)
            }
        }
    }

    fun fetchTariffPlans() = viewModelScope.launch {
        val tariffResponse = subscriptionRepository.getAllTariffPlans()

        when(tariffResponse) {
            is ApiResult.Success -> {
                val tariffPlansList = tariffResponse.data.map { it.toTariffPlan() }
                _uiState.update { it.copy(tariffPlansList = tariffPlansList) }
            }
            is ApiResult.Error -> { println("Ошибка при получении списка тарифов") }
        }
    }

    private fun updateActiveSubscriptionState(newValue: ActiveSubscription?) {
        _uiState.update { it.copy(activeSubscription = newValue) }
    }
}