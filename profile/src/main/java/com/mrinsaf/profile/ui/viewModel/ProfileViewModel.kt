package com.mrinsaf.profile.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.repository.local.UserInfoStorage
import com.mrinsaf.core.data.repository.network.UziServiceRepository
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
    private val repository: UziServiceRepository,
    @ApplicationContext val context: Context
): ViewModel() {
    private var _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
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
}