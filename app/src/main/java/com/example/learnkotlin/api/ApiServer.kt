package com.example.learnkotlin.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.util.Base64

object ApiClient {
    // Change this if you host under a different base path
    const val BASE_URL = "https://user-10vg.onrender.com"

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val http = HttpClient(Android) {
        install(ContentNegotiation) {
            json(json)
        }
        install(DefaultRequest) {
            url(BASE_URL)
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 30_000
            requestTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }
        // Note: Attach Authorization header per request in repository when needed
    }
}

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    // Common token field names
    val token: String? = null,
    @SerialName("access_token") val accessTokenSnake: String? = null,
    @SerialName("accessToken") val accessTokenCamel: String? = null,
    // Expiry styles
    val exp: Long? = null,                // epoch seconds
    @SerialName("expires_in") val expiresInSnake: Long? = null, // seconds from now
    @SerialName("expiresIn") val expiresInCamel: Long? = null,
    val user: JsonObject? = null
) {
    fun resolvedToken(): String? = token ?: accessTokenSnake ?: accessTokenCamel
    fun resolvedExpiresInSeconds(): Long? = expiresInSnake ?: expiresInCamel
}

object JwtUtils {
    fun extractExpEpochSeconds(jwt: String): Long? {
        return try {
            val parts = jwt.split('.')
            if (parts.size < 2) return null
            val decoder = Base64.getUrlDecoder()
            val payloadJson = String(decoder.decode(parts[1]))
            val element = ApiClient.json.parseToJsonElement(payloadJson)
            val exp = element.jsonObject["exp"]?.jsonPrimitive?.longOrNull
            exp
        } catch (e: Exception) {
            null
        }
    }
}