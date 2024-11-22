package com.example.uzi.ui.viewModel.main

import androidx.lifecycle.ViewModel
import com.example.uzi.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

//class MainViewModel: ViewModel() {
//    private var _uiState = MutableStateFlow(MainUiState())
//    val uiState: StateFlow<MainUiState>
//        get() = _uiState
//
//    fun onSuccessfulLogin(
//        userName: String,
//        userEmail: String,
//    ) {
//        _uiState.update { it.copy(
//            user = User(
//                userName = userName,
//                userEmail = userEmail
//            )
//        ) }
//    }
//}