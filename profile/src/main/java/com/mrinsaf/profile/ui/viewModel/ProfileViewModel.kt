package com.mrinsaf.profile.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mrinsaf.core.data.data_source.local.UserInfoStorage
import com.mrinsaf.core.domain.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UziServiceRepository,
    @ApplicationContext val context: Context
): ViewModel() {
    private var _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState>
        get() = _uiState

    suspend fun loadUserInfo() {
        println("Достаю информацию о пользователе")
        try {
            val userId = UserInfoStorage.getUserId(context).first()
            val userInfo = repository.getPatient(userId)
            println(userInfo)

            _uiState.update { it.copy(userInfo) }
        }
        catch (e: Exception) {
            println("Ошибка при получении информации о пользователе $e")
        }
    }
}