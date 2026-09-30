package com.example.nexusappxml.data.model

import com.google.gson.annotations.SerializedName

data class BattleResolveResponse(
    @SerializedName("floor") val floor: Int,
    @SerializedName("is_boss") val isBoss: Boolean,
    @SerializedName("place_image") val placeImage: String,
    @SerializedName("player_power") val playerPower: Int,
    @SerializedName("enemy_power") val enemyPower: Int,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)