package com.mrinsaf.core.data.mock

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import com.mrinsaf.core.domain.model.basic.Node
import com.mrinsaf.core.domain.model.basic.Uzi
import com.mrinsaf.core.domain.model.basic.UziImage
import kotlin.random.Random
import androidx.core.graphics.createBitmap
import com.mrinsaf.core.data.network.dto.network_responses.NodesSegmentsResponse
import com.mrinsaf.core.domain.model.basic.SectorPoint
import com.mrinsaf.core.domain.model.basic.Segment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.ByteArrayOutputStream

object MockUziServiceDataSource {
    fun getPatientUzis(patientId: String): List<Uzi> {
        return patientUzis.filter { it.externalId == patientId }
    }

    fun getUziNodes(uziId: String): List<Node> {
        return mockNodes[uziId] ?: emptyList()
    }

    fun getUziImages(uziId: String): List<UziImage> {
        return uziImages[uziId] ?: emptyList()
    }

    fun createUzi(
        uziUris: Uri,
        projection: String,
        patientId: String,
        deviceId: String
    ) {
        val uziId = "uzi_${patientUzis.size + 1}"
        patientUzis.add(
            Uzi(
                id = uziId,
                externalId = patientId,
                authorId = patientId,
                projection = projection,
                createAt = "${System.currentTimeMillis()}",
                deviceId = 1,
                status = "new",
            )
        )

    }

    fun downloadUziImage(uziId: String, imageId: String): ResponseBody {
        val bitmap = userBitmaps[imageId] ?: defaultBitmap
        val byteArray = ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
        }
        return byteArray.toByteArray().toResponseBody("image/png".toMediaType())
    }

    fun getImageNodesAndSegments(
        imageId: String,
        diagnosticCompleted: Boolean
    ): NodesSegmentsResponse {
        val uziId = findUziIdByImageId(imageId)
            ?: throw IllegalArgumentException("No UZI found for imageId: $imageId")
        val nodes = mockNodes[uziId] ?: emptyList()
        val segments = generateMockSegments(imageId, nodes)
        return NodesSegmentsResponse(nodes, segments)
    }

    private fun findUziIdByImageId(imageId: String): String? {
        return uziImages.entries.firstOrNull { (_, images) ->
            images.any { it.id == imageId }
        }?.key
    }

    private val patientUzis: MutableList<Uzi> = mutableListOf(
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

    // Дефолтное изображение (белый квадрат 100x100)
    private val defaultBitmap = createBitmap(100, 100).apply {
        eraseColor(Color.WHITE)
    }

    private val userBitmaps = mutableMapOf<String, Bitmap>()

    private val mockNodes: Map<String, List<Node>> = generateMockNodes(patientUzis.map { it.id })

    private val uziImages: Map<String, MutableList<UziImage>> =
        generateMockImages(patientUzis.map { it.id }, imagesPerUzi = 3)

    private fun generateMockNodes(
        uziIds: List<String>,
        nodesPerUzi: Int = 2
    ): Map<String, List<Node>> {
        return uziIds.associateWith { uziId ->
            (1..nodesPerUzi).map { index ->
                val dominantTirads = listOf("TIRADS_23", "TIRADS_4", "TIRADS_5").random()
                val (tirads23, tirads4, tirads5) = generateTirads()

                Node(
                    id = "node_${uziId}_$index",
                    ai = Random.nextBoolean(),
                    validation = if (Random.nextInt(10) < 3) null else listOf("valid", "invalid").random(),
                    description = "Узел с преобладанием $dominantTirads",
                    uziId = uziId,
                    tirads23 = tirads23,
                    tirads4 = tirads4,
                    tirads5 = tirads5
                )
            }
        }
    }

    // Генерация моковых сегментов
    private fun generateMockSegments(imageId: String, nodes: List<Node>): List<Segment> {
        return (1..Random.nextInt(3, 6)).map { segmentIndex ->
            val nodeCount = Random.nextInt(0, 3)
            val selectedNodes = nodes.shuffled().take(nodeCount)
            val segmentId = "segment_${imageId}_$segmentIndex"
            val (tirads23, tirads4, tirads5) = generateTirads()

            Segment(
                id = segmentId,
                image_id = imageId,
                node_id = if (selectedNodes.isNotEmpty()) selectedNodes.first().id else "no_node",
                contor = generateContourPoints(),
                ai = Random.nextBoolean(),
                tirads23 = tirads23,
                tirads4 = tirads4,
                tirads5 = tirads5
            )
        }.toList()
    }

    private fun generateMockImages(uziIds: List<String>, imagesPerUzi: Int = 3): Map<String, MutableList<UziImage>> {
        return uziIds.associateWith { uziId ->
            (1..imagesPerUzi).map { index ->
                UziImage(
                    id = "img_${uziId}_$index",
                    page = index
                )
            } as MutableList<UziImage>
        }
    }

    // Генерация случайных коэффициентов TIRADS
    private fun generateTirads(): Triple<Double, Double, Double> {
        val dominant = listOf("TIRADS_23", "TIRADS_4", "TIRADS_5").random()
        return when (dominant) {
            "TIRADS_23" -> Triple(0.7, 0.2, 0.1)
            "TIRADS_4" -> Triple(0.3, 0.6, 0.1)
            else -> Triple(0.2, 0.3, 0.5)
        }
    }

    private fun generateContourPoints(): List<SectorPoint> {
        return (1..10).map {
            SectorPoint(
                x = Random.nextInt(100),
                y = Random.nextInt(100)
            )
        }
    }
}