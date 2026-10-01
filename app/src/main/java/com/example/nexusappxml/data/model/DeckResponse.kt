package com.example.nexusappxml.data.model

import com.google.gson.annotations.SerializedName

data class DeckResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("heroes") val heroes: List<HeroItemResponse>?
)

data class HeroItemResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("image_url") val imageUrl: String?
)