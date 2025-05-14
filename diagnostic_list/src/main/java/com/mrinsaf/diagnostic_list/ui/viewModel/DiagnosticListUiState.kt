package com.mrinsaf.diagnostic_list.ui.viewModel

import com.mrinsaf.core.domain.model.basic.NodesWithUziId
import com.mrinsaf.core.domain.model.basic.Uzi

data class DiagnosticListUiState(
    var uziList: List<Uzi> = emptyList(),

    var nodesWithUziId: List<NodesWithUziId> = emptyList()
)