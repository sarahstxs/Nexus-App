package com.example.nexusappxml.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String?,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("user_id")
    val userId: Int
)
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val admin: Boolean,
    val active: Boolean,
    val highest_level: Int,
    val coins: Int,
    val create_date: String,
    val current_level: Int
)

data class RegisterResponse(
    val message: String
)

data class UserResponse(
    val username: String,
    val email: String,
    val coins: Int,
    val current_level: Int
    // Adicione os outros campos que o seu 'listUSer' retorna do banco
)