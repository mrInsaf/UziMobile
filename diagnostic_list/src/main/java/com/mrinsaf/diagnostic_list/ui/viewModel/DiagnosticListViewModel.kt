package com.mrinsaf.diagnostic_list.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.domain.model.basic.NodesWithUziId
import com.mrinsaf.core.domain.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosticListViewModel @Inject constructor(
    private val repository: UziServiceRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(DiagnosticListUiState())
    val uiState: StateFlow<DiagnosticListUiState>
        get() = _uiState

    fun getPatientUzis(patientId: String) {
        viewModelScope.launch {
            try {
                val uziList = repository.getPatientUzis(patientId)
                val nodesWithUziIdsDeferred = mutableListOf<Deferred<NodesWithUziId?>>()

                // Создаем отдельную корутину для каждой диагностики
                uziList.forEach { uzi ->
                    val deferred = async(Dispatchers.IO) {
                        try {
                            val id = uzi.id
                            val nodes = repository.getUziNodes(id)
                            NodesWithUziId(
                                uziId = id,
                                nodes = nodes
                            )
                        } catch (e: Exception) {
                            println("Ошибка при получении узлов для UZI ID=${uzi.id}: ${e.message}")
                            null // Возвращаем null, если произошла ошибка
                        }
                    }
                    nodesWithUziIdsDeferred.add(deferred)
                }

                val nodesWithUziIds = nodesWithUziIdsDeferred.awaitAll().filterNotNull()

                _uiState.update {
                    it.copy(
                        uziList = uziList,
                        nodesWithUziId = nodesWithUziIds
                    )
                }
            } catch (e: Exception) {
                println("Ошибка при получении списка UZI: ${e.message}")
            }
        }
    }
}