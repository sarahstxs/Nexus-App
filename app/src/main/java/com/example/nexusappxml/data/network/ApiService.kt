package com.example.nexusappxml.data.network

import com.example.nexusappxml.data.model.LoginRequest
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Body

// Modelo de dados que recebe a resposta do token do FastAPI
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)

interface ApiService {
    @POST("/users/login") // Rota exata na FastAPI
    suspend fun login(
        @Body request: LoginRequest
    ): TokenResponse
}