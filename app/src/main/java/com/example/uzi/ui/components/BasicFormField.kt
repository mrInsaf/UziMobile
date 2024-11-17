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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uzi.ui.screens.passwordRestrictions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicFormField(
    value: String,
    AdditionalContent: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(bottom = 12.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        if (AdditionalContent != null) {
            Spacer(modifier = Modifier.size(16.dp))
            AdditionalContent()
        }
    }
}

@Preview
@Composable
fun BasicFormFieldPreview() {
    BasicFormField(
        value = "",
        AdditionalContent = {
            Text(
                text = passwordRestrictions,
                modifier = Modifier
            )
        }
    ) {

    }
}