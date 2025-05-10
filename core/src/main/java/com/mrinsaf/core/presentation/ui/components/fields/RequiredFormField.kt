package com.mrinsaf.core.presentation.ui.components.fields

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.R

@Composable
fun RequiredFormField(
    modifier: Modifier = Modifier,
    value: String,
    AdditionalContent: (@Composable () -> Unit)? = null,
    label: String,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    val annotatedLabel = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("* ")
        }
        append(label)
    }

    var wasFocused by remember { mutableStateOf(false) }
    var focusedCount by remember { mutableStateOf(0) }

    BasicFormField(
        value = value,
        AdditionalContent = AdditionalContent,
        label = annotatedLabel,
        isError = isError || (focusedCount > 2 && value.isBlank()),
        modifier = modifier.onFocusChanged { focusState ->
            if (focusedCount < 3) {
                focusedCount++
            }
        }
    ) {
        onValueChange(it)
    }
}