package com.mrinsaf.core.data.mock

import com.mrinsaf.core.domain.model.basic.Node
import com.mrinsaf.core.domain.model.basic.Uzi

object MockUziServiceDataSource {
    fun getPatientUzis(patientId: String): List<Uzi> {
        return patientUzis.filter { it.externalId == patientId }
    }

    fun getUziNodes(uziId: String): List<Node> {
        return mockNodes[uziId] ?: emptyList()
    }

    private val patientUzis: List<Uzi> = mutableListOf(
        Uzi(
            id = "uzi_1",
            externalId = "stepanMorkow",
            authorId = "stepanMorkow",
            projection = "1",
            createAt = "2024-12-25T10:30:00",
            deviceId = 1,
            status = "completed",
            checked = true,
        )
    )

    private val mockNodes: Map<String, List<Node>> = mapOf(
        "uzi_1" to listOf(
            Node(
                id = "node_1",
                ai = true,
                validation = "valid",
                description = "Узел с преобладанием TIRADS 2",
                uziId = "uzi_1",
                tirads23 = 0.9,  // Преобладает
                tirads4 = 0.3,
                tirads5 = 0.2
            ),
            Node(
                id = "node_2",
                ai = false,
                validation = null,
                description = "Узел с преобладанием TIRADS 4",
                uziId = "uzi_1",
                tirads23 = 0.4,
                tirads4 = 0.7,  // Преобладает
                tirads5 = 0.3
            )
        ),
        "uzi_2" to listOf(
            Node(
                id = "node_3",
                ai = true,
                validation = "invalid",
                description = "Узел с преобладанием TIRADS 5",
                uziId = "uzi_2",
                tirads23 = 0.2,
                tirads4 = 0.5,
                tirads5 = 0.8  // Преобладает
            )
        )
    )
}