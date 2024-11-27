package com.example.uzi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.uzi.R
import com.example.uzi.data.mock.mockReportResponse
import com.example.uzi.data.models.SectorPoint
import com.example.uzi.ui.components.canvas.ZoomableCanvasSectorWithConstraints
import com.example.uzi.ui.components.containers.FormationInfoContainer
import com.example.uzi.ui.theme.Paddings


@Composable
fun DiagnosticScreen(
    diagnosticDate: String,
    clinicName: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(horizontal = Paddings.Medium)
    ) {
        var isFullScreenOpen by remember { mutableStateOf(false) }
        TextButton(
            onClick = {
//                onAndroidBackClick(
//                    navController = navController,
//                    viewModel = newDiagnosticViewModel
//                )
            },
//            enabled = newDiagnosticUiState.currentScreenIndex > 0
        ) {
            Text(
                text = "Назад",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Text(
            text = "Диагностика от $diagnosticDate",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = clinicName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
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

            val points = listOf(
                SectorPoint(100, 100),
                SectorPoint(200, 100),
                SectorPoint(200, 200),
                SectorPoint(100, 300)
            )


            ZoomableCanvasSectorWithConstraints(
                imageBitmap = ImageBitmap.imageResource(R.drawable.paint),
                pointsList = if (selectedTabIndex == 1) emptyList() else points,
                onFullScreen = { isFullScreenOpen = true },
            )

//          TODO(Здесь надо пользоваться viewModel с репозиторием)
            mockReportResponse.formations.forEachIndexed { i, formation ->
                val maxTirads = maxOf(
                    formation.tirads.tirads_23,
                    formation.tirads.tirads_4,
                    formation.tirads.tirads_5
                )

                val formationClass = when (maxTirads) {
                    formation.tirads.tirads_23 -> 2 // TIRADS 2-3
                    formation.tirads.tirads_4 -> 4 // TIRADS 4
                    formation.tirads.tirads_5 -> 5 // TIRADS 5
                    else -> 0 // На случай ошибки
                }

                FormationInfoContainer(
                    formationIndex = i,
                    formationClass = formationClass,
                    formationProbability = maxTirads,
                    formationDescription = "Описание формации"
                )
            }
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
//
//sealed class DiagnosticScreen(val route: String) {
//    object MainScreen : DiagnosticScreen("main_screen")
//    object ImageFullScreen : DiagnosticScreen("image_full_screen")
//}


@Preview
@Composable
fun DiagnosticScreenPreview() {
    DiagnosticScreen(
        diagnosticDate = "24.11.2024",
        clinicName = "Клиника"
    )
}