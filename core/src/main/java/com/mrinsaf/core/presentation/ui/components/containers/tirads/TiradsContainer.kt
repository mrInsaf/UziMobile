package com.mrinsaf.core.presentation.ui.components.containers.tirads

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.presentation.ui.theme.UziTheme
import com.mrinsaf.core.presentation.ui.theme.errorBackgroundColor
import com.mrinsaf.core.presentation.ui.theme.errorTextColor
import com.mrinsaf.core.presentation.ui.theme.fallbackBackgroundColor
import com.mrinsaf.core.presentation.ui.theme.fallbackTextColor
import com.mrinsaf.core.presentation.ui.theme.successBackgroundColor
import com.mrinsaf.core.presentation.ui.theme.successTextColor
import com.mrinsaf.core.presentation.ui.theme.warningBackgroundColor
import com.mrinsaf.core.presentation.ui.theme.warningTextColor

@Composable
fun TiradsContainer(
    formationClass: Int,
    formationProbability: Int,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    val (textColor, backgroundColor) = when (formationClass) {
        in 1..3 -> successTextColor to successBackgroundColor
        4 -> warningTextColor to warningBackgroundColor
        5 -> errorTextColor to errorBackgroundColor
        else -> fallbackTextColor to fallbackBackgroundColor
    }
    BasicTiradsContainer(
        text = "EU TIRADS $formationClass - $formationProbability%",
        textColor = textColor,
        backgroundColor = backgroundColor,
        textStyle = textStyle
    )
}

@Preview
@Composable
fun TiradsContainerPreview() {
    UziTheme {
        TiradsContainer(
            formationClass = 1,
            formationProbability = 90,
        )
    }
}