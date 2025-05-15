package com.mrinsaf.auth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel
import com.mrinsaf.core.presentation.ui.components.MainButton
import com.mrinsaf.core.presentation.ui.components.fields.RequiredFormField

@Composable
fun AuthorizationScreen(
    authorisationViewModel: AuthorisationViewModel,
    onSubmitLoginButtonClick: () -> Unit,
    onRegistrationButtonClick: () -> Unit,
) {
    val authorisationUiState = authorisationViewModel.uiState.collectAsState().value
    Scaffold { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    //                .fillMaxSize()
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

                    MainButton(text = "Войти") {
                        onSubmitLoginButtonClick()
                    }

                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "У вас нет аккаунта?"
                )
                TextButton(
                    onClick = {
                        onRegistrationButtonClick()
                    }
                ) {
                    Text(text = "Зарегистрироваться")
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun AuthorizationScreenPreview() {
//    AuthorizationScreen(
//        onSubmitLoginButtonClick = {},
//        authorisationViewModel = AuthorisationViewModel(
//            repository = MockUziServiceRepository(),
//            context =
//        )
//    )
//}