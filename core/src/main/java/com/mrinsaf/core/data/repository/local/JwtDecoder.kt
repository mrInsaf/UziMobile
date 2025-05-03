package com.mrinsaf.core.data.repository.local
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.Claim

object JwtDecoder {

    fun getTokenId(token: String): String {
        return try {
            val jwt = JWT.decode(token)

            val idClaim: Claim = jwt.getClaim("id")

            return if (!idClaim.isNull) idClaim.asString() else throw Exception("Decoded user id is null")
        } catch (e: JWTDecodeException) {
            println("Ошибка при извлечении ID из токена: ${e.message}")
            throw e
        }
    }
}