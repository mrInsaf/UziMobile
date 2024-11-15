package com.example.uzi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uzi.ui.components.RequiredFormField
import com.example.uzi.ui.viewModel.authorisation.AuthorisationViewModel

@Composable
fun AuthorizationScreen(
    authorisationViewModel: AuthorisationViewModel = AuthorisationViewModel()
) {
    val authorisationUiState = authorisationViewModel.uiState.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(Modifier.size(80.dp))
        Text(
            text = "Виртуальный ассистент",
            color = Color.LightGray,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(40.dp))
        Text(
            text = "Авторизация",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.size(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RequiredFormField(
                value = authorisationUiState.authorizationEmail,
                label = "Электронная почта",
            ) {
                authorisationViewModel.onAuthorizationEmailChange(it)
            }

            RequiredFormField(
                value = authorisationUiState.authorizationPassword,
                label = "Пароль",
            ) {
                authorisationViewModel.onAuthorizationPasswordChange(it)
            }

            Button(
                onClick = {  },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp)
            ) {
                Text(text = "Войти")
            }

        }
    }
}

@Preview
@Composable
fun AuthorizationScreenPreview() {
    AuthorizationScreen()
}