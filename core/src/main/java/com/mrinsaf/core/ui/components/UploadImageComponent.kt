package com.mrinsaf.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.R

@Composable
fun UploadImageComponent(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.LightGray)
    ) {
        Spacer(Modifier)
        Icon(
            painter = painterResource(R.drawable.archive),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = "Нажмите в эту область для загрузки",
            textAlign = TextAlign.Center,
            modifier = Modifier.width(250.dp)
        )
        Text(
            text = "Выберите файл в формате .png или .tiff",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.width(250.dp)
        )
        Spacer(Modifier)
    }
}