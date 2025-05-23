package com.mrinsaf.diagnostic_details.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mrinsaf.core.R
import com.mrinsaf.core.domain.model.basic.SectorPoint
import com.mrinsaf.core.data.network.repository.MockUziServiceRepository
import com.mrinsaf.core.presentation.ui.components.LoadingAnimation
import com.mrinsaf.core.presentation.ui.components.bottomSheet.RecommendationBottomSheet
import com.mrinsaf.core.presentation.ui.components.canvas.ZoomableCanvasSectorWithConstraints
import com.mrinsaf.core.presentation.ui.components.containers.FormationInfoContainer
import com.mrinsaf.core.presentation.ui.theme.Paddings
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel


@SuppressLint("NewApi")
@Composable
fun DiagnosticScreen(
    diagnosticDate: String,
    clinicName: String,
    diagnosticViewModel: DiagnosticViewModel,
    onBackButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var isFullScreenOpen by remember { mutableStateOf(false) }
        val uiState = diagnosticViewModel.uiState.collectAsState().value

        TextButton(
            onClick = {
                onBackButtonClick()
            }
        ) {
            Text(
                text = "Назад",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Text(
            text = "Диагностика от $diagnosticDate",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
        Text(
            text = clinicName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = Color.Black
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Paddings.Medium)
        ) {
            var selectedTabIndex by remember { mutableStateOf(0) }
            val tabs = listOf("Обработанный снимок", "Исходный снимок")

            TabRow(
                selectedTabIndex = selectedTabIndex,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) } // Текст вкладки
                    )
                }
            }

            val currentPage = remember { mutableStateOf(0) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                var sliderPosition by remember { mutableFloatStateOf(0f) }

                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        currentPage.value = it.toInt()
                    },
                    valueRange = 0f..uiState.numberOfImages.toFloat() - 1
                )

                Text(
                    text = "Страница: ${currentPage.value + 1}",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (uiState.uziImagesBmp.size > currentPage.value) {
                val currentImageId = uiState.uziImages.getOrNull(currentPage.value)?.id

                currentImageId?.let { imageId ->
                    uiState.uziImagesBmp[imageId]?.let { bitmap ->
                        val pointsList = if (selectedTabIndex == 1) {
                            emptyList()
                        } else {
                            try {
                                uiState.selectedUziNodesAndSegments.asSequence()
                                    .flatMap { it.segments.asSequence() }
                                    .filter { it.image_id == imageId }
                                    .map { it.contor }
                                    .flatten()
                                    .toList()
                            } catch (e: Exception) {
                                println(e)
                                throw e
                            }
                        }

                        ZoomableCanvasSectorWithConstraints(
                            imageBitmap = bitmap.asImageBitmap(),
                            pointsList = pointsList,
                            onFullScreen = { isFullScreenOpen = true },
                        )
                    }
                }
            } else {
                LoadingAnimation()
            }

            val nodes = uiState.selectedUziNodesAndSegments
                .flatMap { it.segments }
                .filter { segment ->
                    segment.image_id == uiState.uziImages[currentPage.value].id
                }.mapNotNull { segment ->
                    uiState.selectedUziNodesAndSegments
                        .flatMap { it.nodes }
                        .firstOrNull { node -> node.id == segment.node_id }
                }

            nodes.forEachIndexed { i, formation ->
                FormationInfoContainer(
                    formationIndex = i,
                    formationClass = formation.formationClass,
                    formationProbability = formation.maxTirads.times(100).toInt(),
                    formationDescription = stringResource(R.string.shortRecommendationForPatient),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { diagnosticViewModel.openRecommendationBottomSheet() }
                )
            }
            RecommendationBottomSheet(
                isVisible = uiState.isRecommendationSheetVisible,
                onDismiss = { diagnosticViewModel.closeRecommendationBottomSheet() }
            )
        }

        if (isFullScreenOpen) {
            Dialog(onDismissRequest = { isFullScreenOpen = false }) {
                UziImageFullScreen(
                    imageBitmap = ImageBitmap.imageResource(R.drawable.paint),
                    pointsList = listOf(
                        SectorPoint(100, 100),
                        SectorPoint(200, 100),
                        SectorPoint(200, 200),
                        SectorPoint(100, 300)
                    )
                )
            }
        }
    }
}
