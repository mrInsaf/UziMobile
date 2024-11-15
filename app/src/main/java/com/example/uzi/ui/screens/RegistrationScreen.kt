package com.example.uzi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uzi.R
import com.example.uzi.ui.components.RequiredFormField

@Composable
fun RegistrationScreen() {
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
            RequiredFormField("Фамилия")
            RequiredFormField("Имя")
            RequiredFormField("Отчество")
            RequiredFormField("Электронная почта")
            RequiredFormField("Пароль") {
                Text(
                    text = passwordRestrictions,
                    color = Color.LightGray
                )
            }
            RequiredFormField("Повторите пароль")

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
