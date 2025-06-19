package com.mrinsaf.core.data.mock

import com.mrinsaf.core.data.network.dto.network_responses.LoginResponse
import com.mrinsaf.core.data.utils.local.JwtEncoder
import com.mrinsaf.core.domain.model.basic.UziDevice

object MockAuthDataSource {
    private val registeredUsers = mutableListOf(
        MockUser(
            userId = "stepanMorkow",
            userName = "Степан Аркадьевич Морковка",
            email = "sm@mail.ru",
            password = "1234"
        )
    )

    private val uziDevices: List<UziDevice> = listOf(
//        UziDevice(
//            id = 123,
//            name = "siemens"
//        )
    )

    fun login(email: String, password: String): LoginResponse {
        val user = registeredUsers
            .find { it.email == email } ?: throw Exception("email: not found")

        if (password == user.password) return LoginResponse(
            accessToken = JwtEncoder.generateToken(user.userId),
            refreshToken = "mock_refresh_token"
        )
        else throw Exception("email: not found")
    }

    fun registerUser(user: MockUser) {
        registeredUsers.add(user)
    }

    fun getUserIdByEmail(email: String): String? = registeredUsers.find { it.email == email }?.userId

    fun getUziDevices(): List<UziDevice> = uziDevices
}