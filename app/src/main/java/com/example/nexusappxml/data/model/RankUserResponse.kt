package com.example.nexusappxml.data.model

data class RankResponse(
    val ranking: List<RankUser>
)

data class RankUser(
    val position: Int,
    val name: String,
    val level: Int
)