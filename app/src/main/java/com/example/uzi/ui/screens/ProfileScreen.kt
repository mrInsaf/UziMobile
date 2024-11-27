package com.example.uzi.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.ui.components.fields.ProfileField

@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
//            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = "Профиль",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        ProfileField(
            title = "ФИО",
            content = userName,
        )

        ProfileField(
            title = "Электронная почта",
            content = userEmail,
        )

    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        userName = "Иванов Иван Иванович",
        userEmail = "ivanov@mail.ru"
    )
}