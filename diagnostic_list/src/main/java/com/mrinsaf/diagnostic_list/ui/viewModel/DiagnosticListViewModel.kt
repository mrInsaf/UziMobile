package com.mrinsaf.diagnostic_list.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.domain.model.basic.NodesWithUziId
import com.mrinsaf.core.domain.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
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

    private var loadJob: Job? = null

    fun getPatientUzis(patientId: String) {
        println("Запущена функция getPatientUzis")
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            try {
                val uziList = repository.getPatientUzis(patientId)
                val uziIds = uziList.map { it.id }
                println("uziids: $uziIds")
                val nodesWithUziIdsDeferred = mutableListOf<Deferred<NodesWithUziId?>>()

                // Создаем отдельную корутину для каждой диагностики
                uziList.forEach { uzi ->
                    println("Запрашиваю ноды для ${uzi.id}")
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