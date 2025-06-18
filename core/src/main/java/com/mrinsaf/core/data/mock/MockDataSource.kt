package com.mrinsaf.core.data.mock

import com.mrinsaf.core.data.network.dto.network_responses.LoginResponse

object MockDataSource {
    private val registeredUsers = mutableListOf(
        MockUser(
            userId = "lol",
            userName = "Степан Аркадьевич Морковка",
            email = "sm@mail.ru",
            password = "1234"
        )
    )

    fun login(email: String, password: String): LoginResponse {
        val user = registeredUsers
            .find { it.email == email } ?: throw Exception("email: not found")

        if (password == user.password) return LoginResponse(
            accessToken = "mock_access_token",
            refreshToken = "mock_refresh_token"
        )
        else throw Exception("email: not found")
    }

    fun registerUser(user: MockUser) {
        registeredUsers.add(user)
    }
}