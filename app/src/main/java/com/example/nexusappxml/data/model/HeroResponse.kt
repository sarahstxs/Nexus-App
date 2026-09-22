package com.example.nexusappxml.data.model

data class HeroResponse(
    val page: Int,
    val limit: Int,
    val images: List<String> // A lista de URLs prontas para o Glide
)