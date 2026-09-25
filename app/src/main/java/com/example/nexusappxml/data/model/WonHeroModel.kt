package com.example.nexusappxml.data.model

import com.google.gson.annotations.SerializedName

// Apenas uma data class comum, sem Parcelable
data class WonHeroModel(
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String
)