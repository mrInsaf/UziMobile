package com.example.uzi.ui.viewModel.diagnosticList

import com.example.uzi.data.models.basic.NodesWithUziId
import com.example.uzi.data.models.basic.Uzi

data class DiagnosticListUiState(
    var uziList: List<Uzi> = emptyList(),

    var nodesWithUziId: List<NodesWithUziId> = emptyList()
)