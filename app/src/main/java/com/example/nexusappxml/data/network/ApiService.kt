package com.example.nexusappxml.data.network

import com.example.nexusappxml.data.model.HeroResponse
import com.example.nexusappxml.data.model.LoginRequest
import com.example.nexusappxml.data.model.RegisterRequest
import com.example.nexusappxml.data.model.RegisterResponse
import com.example.nexusappxml.data.model.UserResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Modelo de dados que recebe a resposta do token do FastAPI
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("user_id") val userId: Int
)

interface ApiService {
    @POST("/users/login") // Rota exata na FastAPI
    suspend fun login(
        @Body request: LoginRequest
    ): TokenResponse

    @POST("/users/")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("/users/list/{id_user}")
    suspend fun getUserDetails(
        @Path("id_user") id: Int
    ): Response<UserResponse>

    @GET("/heroes/list-all-heroes") // Confirme se a rota no Python é exatamente esta
    suspend fun getHeroes(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<HeroResponse>
}
