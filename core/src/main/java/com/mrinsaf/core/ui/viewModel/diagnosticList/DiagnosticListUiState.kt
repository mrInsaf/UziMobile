package com.mrinsaf.core.ui.viewModel.diagnosticList

import com.mrinsaf.core.data.models.basic.NodesWithUziId
import com.mrinsaf.core.data.models.basic.Uzi

data class DiagnosticListUiState(
    var uziList: List<Uzi> = emptyList(),

    var nodesWithUziId: List<NodesWithUziId> = emptyList()
)