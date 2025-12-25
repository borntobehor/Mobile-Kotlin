package com.example.learnkotlin.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class CatalogRepository {
    suspend fun getPerfumes(): List<PerfumeDto> = withContext(Dispatchers.IO) {
        try {
            CatalogApi.http.get("/perfumes").body()
        } catch (e: io.ktor.client.plugins.ClientRequestException) {
            val body = e.response.bodyAsText()
            error("Get products failed (${e.response.status.value}): $body")
        } catch (e: io.ktor.client.plugins.ServerResponseException) {
            val body = e.response.bodyAsText()
            error("Server error (${e.response.status.value}): $body")
        } catch (e: io.ktor.client.plugins.HttpRequestTimeoutException) {
            error("Products request timed out. The server may be waking up. Try again.")
        }
    }

    suspend fun getPerfume(id: String): PerfumeDto = withContext(Dispatchers.IO) {
        try {
            CatalogApi.http.get("/perfumes/$id").body()
        } catch (e: io.ktor.client.plugins.ClientRequestException) {
            val body = e.response.bodyAsText()
            error("Get product failed (${e.response.status.value}): $body")
        } catch (e: io.ktor.client.plugins.ServerResponseException) {
            val body = e.response.bodyAsText()
            error("Server error (${e.response.status.value}): $body")
        } catch (e: io.ktor.client.plugins.HttpRequestTimeoutException) {
            error("Product request timed out. Try again.")
        }
    }

    suspend fun getGrouped(): GroupedCatalog = withContext(Dispatchers.IO) {
        try {
            CatalogApi.http.get("/perfumes/grouped").body()
        } catch (e: io.ktor.client.plugins.ClientRequestException) {
            val body = e.response.bodyAsText()
            error("Grouped failed (${e.response.status.value}): $body")
        } catch (e: io.ktor.client.plugins.ServerResponseException) {
            val body = e.response.bodyAsText()
            error("Server error (${e.response.status.value}): $body")
        } catch (e: io.ktor.client.plugins.HttpRequestTimeoutException) {
            error("Grouped request timed out. Try again.")
        }
    }

    // New: fetch with optional filters used by Home categories
    suspend fun getPerfumesFiltered(
        gender: String? = null,
        concentration: String? = null,
        popular: Boolean? = null,
        isNew: Boolean? = null,
        limit: Int? = null
    ): List<PerfumeDto> = withContext(Dispatchers.IO) {
        val params = mutableListOf<String>()
        if (!gender.isNullOrBlank()) params += "gender=$gender"
        if (!concentration.isNullOrBlank()) params += "concentration=$concentration"
        if (popular != null) params += "popular=$popular"
        if (isNew != null) params += "new=$isNew"
        if (limit != null) params += "limit=$limit"
        val qs = if (params.isEmpty()) "" else ("?" + params.joinToString("&"))
        try {
            CatalogApi.http.get("/perfumes$qs").body()
        } catch (e: io.ktor.client.plugins.ClientRequestException) {
            val body = e.response.bodyAsText()
            error("Get products failed (${e.response.status.value}): $body")
        } catch (e: io.ktor.client.plugins.ServerResponseException) {
            val body = e.response.bodyAsText()
            error("Server error (${e.response.status.value}): $body")
        } catch (e: io.ktor.client.plugins.HttpRequestTimeoutException) {
            error("Products request timed out. The server may be waking up. Try again.")
        }
    }
}

@Serializable
data class GroupedCatalog(
    @SerialName("Men Fragrance") val men: Section = Section(),
    @SerialName("Women Fragrance") val women: Section = Section(),
    @SerialName("Unisex Fragrance") val unisex: Section = Section(),
    @SerialName("Eau de Toilette (EDT)") val edt: Section = Section(),
    @SerialName("Eau de Parfum (EDP)") val edp: Section = Section()
) {
    @Serializable
    data class Section(
        @SerialName("Popular") val popular: List<PerfumeDto> = emptyList(),
        @SerialName("New Arrivals") val newArrivals: List<PerfumeDto> = emptyList(),
        @SerialName("All Products") val allProducts: List<PerfumeDto> = emptyList()
    )
}
