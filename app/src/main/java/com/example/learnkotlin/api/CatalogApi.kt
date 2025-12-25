package com.example.learnkotlin.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object CatalogApi {
    // Base URL for your catalog service hosted on Render
    const val BASE_URL = "https://kotlin-microservices.onrender.com"

    private val json = Json {
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
    }
}

@Serializable
data class PerfumeDto(
    val _id: String? = null,
    val name: String,
    val brand: String? = null,
    val description: String? = null,
    val price: Double,
    val stock: Int? = null,
    val gender: String,            // men | women | unisex
    val concentration: String,     // EDT | EDP | PARFUM | EXTRAIT | COLOGNE
    val isPopular: Boolean = false,
    val isNewArrival: Boolean = false,
    val imageUrl: String? = null
)
