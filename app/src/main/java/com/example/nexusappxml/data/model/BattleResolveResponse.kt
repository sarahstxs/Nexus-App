package com.example.nexusappxml.data.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("effect") val effect: String?
)

data class BattleResolveResponse(
    @SerializedName("floor") val floor: Int,
    @SerializedName("is_boss") val isBoss: Boolean,
    @SerializedName("place") val place: PlaceResponse?,
    @SerializedName("player_power") val playerPower: Int,
    @SerializedName("enemy_power") val enemyPower: Int,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)