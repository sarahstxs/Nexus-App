package com.example.nexusappxml.data.model

import com.google.gson.annotations.SerializedName

data class HeroResponse(
    val page: Int,
    val limit: Int,

    @SerializedName("heroes")
    val heroes: List<HeroItem>?
)

data class HeroItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("imageUrl")
    val imageUrl: String
)

data class HeroCompleteResponse(
    @SerializedName("hero")
    val hero: HeroDetails?,

    @SerializedName("user_hero")
    val userHero: UserHeroDetails?,

    @SerializedName("have")
    val have: Boolean
)

data class HeroDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("active") val active: Boolean,
    @SerializedName("name") val name: String,

    @SerializedName("real_name") val realName: String?,
    @SerializedName("deck") val deck: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("origin") val origin: Int?,
    @SerializedName("birth") val birth: String?,
    @SerializedName("appearance") val appearance: Int?,
    @SerializedName("first_appearance_comic") val firstAppearanceComic: String?,
    @SerializedName("image_hero") val imageHero: String?,
    @SerializedName("nemesis") val nemesis: Int?,

    @SerializedName("rarity") val rarity: Int,
    @SerializedName("class") val classHero: Int,
    @SerializedName("hyper_attack") val hyperAttack: Int,
    @SerializedName("base_atk") val baseAtk: Int,
    @SerializedName("base_hp") val baseHp: Int,
    @SerializedName("base_def") val baseDef: Int
)

data class UserHeroDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("hero") val hero: Int,
    @SerializedName("user") val user: Int,

    @SerializedName("current_hp") val currentHp: Int,
    @SerializedName("max_hp") val maxHp: Int,

    @SerializedName("drawback") val drawback: Int?,

    @SerializedName("alive") val alive: Boolean,
    @SerializedName("level") val level: Int,
    @SerializedName("fragments") val fragments: Int,
    @SerializedName("active") val active: Boolean
)