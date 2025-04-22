package com.example.uzi.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    repository: UziServiceRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            repository.getUziDevices()
        }
    }
}