package com.mrinsaf.auth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrinsaf.core.ui.components.MainButton
import com.mrinsaf.core.ui.components.fields.RequiredFormField
import com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel
import com.mrinsaf.core.ui.components.fields.dateFormFields.DateFormField
import com.mrinsaf.core.ui.components.fields.dateFormFields.RequiredDateFormField

@Composable
fun RegistrationScreen(
    registrationViewModel: RegistraionViewModel = RegistraionViewModel()
) {
    val registrationUiState = registrationViewModel.uiState.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
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
                onValueChange = { registrationViewModel.onSurnameChange(it) }
            )

            // Имя
            RequiredFormField(
                value = registrationUiState.name,
                label = "Имя",
                onValueChange = { registrationViewModel.onNameChange(it) }
            )

            // Отчество
            RequiredFormField(
                value = registrationUiState.patronymic,
                label = "Отчество",
                onValueChange = { registrationViewModel.onPatronymicChange(it) }
            )

            // Email
            RequiredFormField(
                value = registrationUiState.email,
                label = "Электронная почта",
                isError = registrationUiState.emailError != null,
                onValueChange = { registrationViewModel.onEmailChange(it) }
            )

            // Дата рождения
            RequiredDateFormField(
                label = "Дата рождения",
                onValueChange = { registrationViewModel.onDatePick(it) }
            )

            // Полис
            RequiredFormField(
                value = registrationUiState.policy,
                label = "Номер полиса",
                onValueChange = { registrationViewModel.onPolicyChange(it) }
            )

            // Пароль
            RequiredFormField(
                value = registrationUiState.password,
                label = "Пароль",
                isError = registrationUiState.passwordError != null,
                AdditionalContent = {
                    Text(
                        text = "Минимум 8 символов, буквы и цифры",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                },
                onValueChange = { registrationViewModel.onPasswordChange(it) }
            )

            // Повтор пароля
            RequiredFormField(
                value = registrationUiState.repeatPassword,
                label = "Повторите пароль",
                isError = registrationUiState.repeatPasswordError != null,
                onValueChange = { registrationViewModel.onRepeatPasswordChange(it) }
            )

            val errors = listOfNotNull(
                registrationUiState.blankFieldsError,
                registrationUiState.emailError,
                registrationUiState.passwordError,
                registrationUiState.repeatPasswordError
            ).filter { it.isNotBlank() }

            Column (
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ) {
                Text(
                    text = errors.joinToString("\n"),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }
            Spacer(Modifier.size(12.dp))

            MainButton(
                text = "Создать аккаунт"
            ) {
                registrationViewModel.validateForm()
            }

            Spacer(modifier = Modifier.size(40.dp))
        }
    }
}



@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen()
}
