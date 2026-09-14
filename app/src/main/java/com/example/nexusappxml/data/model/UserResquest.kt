package com.example.nexusappxml.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class LoginRequest(
    val email: String,
    val password: String
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