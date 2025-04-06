package com.mrinsaf.core.ui.components.fields.dateFormFields

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.mrinsaf.core.ui.components.fields.RequiredFormField

@Composable
fun RequiredDateFormField(
    label: String,
    onValueChange: (String) -> Unit,
) {
    val annotatedLabel = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("* ")
        }
        append(label)
    }

    DateFormField(
        label = annotatedLabel
    ) {
        onValueChange(it)
    }
}