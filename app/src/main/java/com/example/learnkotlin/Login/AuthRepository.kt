package com.example.learnkotlin.Login

import com.example.learnkotlin.api.ApiClient
import com.example.learnkotlin.api.AuthResponse
import com.example.learnkotlin.api.JwtUtils
import com.example.learnkotlin.api.LoginRequest
import com.example.learnkotlin.api.RegisterRequest
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository that talks to your Node user-service.
 * It assumes:
 *  - POST /login  with JSON { "email": "...", "password": "..." }
 *  - POST /register with the same body
 *  - Response contains at least a token (JWT or opaque). Optionally includes
 *    either `exp` (epoch seconds) or `expires_in` (seconds from now).
 */
class AuthRepository {
    data class AuthResult(val token: String, val expiryEpochSeconds: Long?)

    suspend fun login(email: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val response: AuthResponse = ApiClient.http.post("/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email = email, password = password))
            }.body()
            parseAuthResponse(response)
        } catch (e: ClientRequestException) { // 4xx
            val body = e.response.bodyAsText()
            error("Login failed (${e.response.status.value}): $body")
        } catch (e: ServerResponseException) { // 5xx
            val body = e.response.bodyAsText()
            error("Server error (${e.response.status.value}): $body")
        } catch (e: HttpRequestTimeoutException) {
            error("Login request timed out. The server may be waking up. Try again.")
        }
    }

    suspend fun register(name: String, email: String, password: String): Unit = withContext(Dispatchers.IO) {
        // We don't require token on register path (often backends just create the user)
        try {
            ApiClient.http.post("/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(name = name, email = email, password = password))
            }
        } catch (e: ClientRequestException) {
            val body = e.response.bodyAsText()
            error("Register failed (${e.response.status.value}): $body")
        } catch (e: ServerResponseException) {
            val body = e.response.bodyAsText()
            error("Server error (${e.response.status.value}): $body")
        } catch (e: HttpRequestTimeoutException) {
            error("Register request timed out. The server may be waking up. Try again.")
        }
    }

    private fun parseAuthResponse(res: AuthResponse): AuthResult {
        val token = res.resolvedToken() ?: error("No token in response")
        // Determine expiry
        val expiryEpochSeconds: Long? = when {
            res.exp != null -> res.exp
            res.resolvedExpiresInSeconds() != null -> {
                val nowSec = System.currentTimeMillis() / 1000
                nowSec + (res.resolvedExpiresInSeconds() ?: 0)
            }
            else -> JwtUtils.extractExpEpochSeconds(token)
        }
        return AuthResult(token, expiryEpochSeconds)
    }
}
