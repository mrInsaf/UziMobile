package com.example.uzi.ui.components.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.R
import com.example.uzi.ui.theme.Paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(start = Paddings.Medium, end = Paddings.Medium, bottom = Paddings.Medium)
        )  {
            Text(
                text = stringResource(R.string.longRecommendationForPatient),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun RecommendationBottomSheetPreview() {
    RecommendationBottomSheet(
        isVisible = true,
        onDismiss = {}
    )
}
