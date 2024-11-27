package com.example.uzi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.uzi.ui.components.MainButton
import com.example.uzi.ui.components.fields.RequiredFormField
import com.example.uzi.ui.viewModel.registraion.RegistraionViewModel

@Composable
fun RegistrationScreen(
    registrationViewModel: RegistraionViewModel = RegistraionViewModel()
) {
    val registrationUiState = registrationViewModel.uiState.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState())

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
            text = "Регистрация",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.size(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
        ) {
            RequiredFormField(
                value = registrationUiState.surname,
                label = "Фамилия",
            ) {
                registrationViewModel.onSurnameChange(it)
            }

            RequiredFormField(
                value = registrationUiState.name,
                label = "Имя",
            ) {
                registrationViewModel.onNameChange(it)
            }
            RequiredFormField(
                value = registrationUiState.patronymic,
                label = "Отчество",
            ) {
                registrationViewModel.onPatronymicChange(it)
            }
            RequiredFormField(
                value = registrationUiState.email,
                label = "Электронная почта",
            ) {
                registrationViewModel.onEmailChange(it)
            }
            RequiredFormField(
                value = registrationUiState.password,
                label = "Пароль",
                AdditionalContent = {
                    Text(
                        text = "Пароль должен содержать хотя бы 8 символов, включая цифры и спецсимволы",
                        color = Color.LightGray
                    )
                }
            ) {
                registrationViewModel.onPasswordChange(it)
            }
            RequiredFormField(
                value = registrationUiState.repeatPassword,
                label = "Повторите пароль",
            ) {
                registrationViewModel.onRepeatPasswordChange(it)
            }
        }

        MainButton(
            text = "Создать аккаунт"
        ) {
            TODO()
        }

    }
}

val passwordRestrictions = "Пароль должен содержать:\n" +
        "- Заглавную букву\n" +
        "- Строчную букву\n" +
        "- Один специальный символ (- # ! \$ @\n" +
        "%^&*+1=?,./)\\\n" +
        "- Минимум 8 знаков"

@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen()
}
