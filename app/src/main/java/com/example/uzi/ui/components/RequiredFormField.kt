package com.example.uzi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uzi.ui.screens.passwordRestrictions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequiredFormField(
    value: String,
    AdditionalContent: (@Composable () -> Unit)? = null,
    label: String,
    onValueChange: (String) -> Unit,
) {
    val annotatedLabel = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("* ")
        }
        append(label)
    }
        BasicFormField(
            value = value,
            AdditionalContent = AdditionalContent,
            label = annotatedLabel.toString(),
        ) {
            onValueChange(it)
        }
}

@Preview
@Composable
fun RequiredFormFieldPreview() {
    RequiredFormField(
        value = "",
        label =  "Электронная почта",
        AdditionalContent = {
            Text(
                text = passwordRestrictions,
                modifier = Modifier
            )
        }
    ) {

    }
}