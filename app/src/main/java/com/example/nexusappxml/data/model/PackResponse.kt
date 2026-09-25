package com.example.nexusappxml.data.model

import com.google.gson.annotations.SerializedName

data class PackResponse(
    @SerializedName("Packs")
    val packs: List<PackItem>
)

data class PackItem(
    val id: Int,
    val name: String,
    val deck: String,
    val active: Boolean,
    val price: Int
)