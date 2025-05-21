package com.mrinsaf.core.presentation.ui.components.fields


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileField(
    title: String,
    textValue: String? = null,
    content: @Composable () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        textValue?.let {
            Text(
                text = textValue,
                fontWeight = FontWeight.Normal
            )
        }
        content()
    }
}

@Preview
@Composable
fun ProfileFieldPreview() {
    ProfileField(
        "Фио", "Электронная почтач"
    )
}