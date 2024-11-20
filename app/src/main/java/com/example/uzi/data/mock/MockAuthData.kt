package com.example.uzi.data.mock

data class MockAuthData(
    var isAuthorised: Boolean = false,
    var isRegistered: Boolean = false,

    var surname: String = "Иванов",
    var name: String = "Иван",
    var patronymic: String = "Иванович",
    var email: String = "ivanov@mail.ru",
    var password: String = "123",

)
