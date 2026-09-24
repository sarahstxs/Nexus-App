package com.example.nexusappxml.data.model

import com.google.gson.annotations.SerializedName

data class HeroResponse(
    val page: Int,
    val limit: Int,
    // ATENÇÃO AQUI: Se a sua API Python estiver a devolver "images": [ ... ]
    // mude o @SerializedName("heroes") para @SerializedName("images")
    @SerializedName("heroes")
    val heroes: List<HeroItem>?
)

data class HeroItem(
    @SerializedName("id")
    val id: Int,

    // ATENÇÃO AQUI: Se a sua API Python estiver a devolver "image_hero": "url..."
    // mude o @SerializedName("imageUrl") para @SerializedName("image_hero")
    @SerializedName("imageUrl")
    val imageUrl: String
)

// 1. A resposta principal (Lê o JSON inteiro)
data class HeroCompleteResponse(
    @SerializedName("hero")
    val hero: HeroDetails?,

    @SerializedName("user_hero")
    val userHero: UserHeroDetails?,

    @SerializedName("have")
    val have: Boolean
)

// 2. Os detalhes base do Herói (Mapeia as colunas da tabela 'Hero')
data class HeroDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("active") val active: Boolean,
    @SerializedName("name") val name: String,

    // Campos que podem ser nulos (nullable=True no Python)
    @SerializedName("real_name") val realName: String?,
    @SerializedName("deck") val deck: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("origin") val origin: Int?,
    @SerializedName("birth") val birth: String?,
    @SerializedName("apperance") val appearance: Int?, // Escrito exatamente como na sua coluna "apperance"
    @SerializedName("first_appearance_comic") val firstAppearanceComic: String?,
    @SerializedName("image_hero") val imageHero: String?,
    @SerializedName("nemesis") val nemesis: Int?,

    // Campos obrigatórios (nullable=False no Python)
    @SerializedName("rarity") val rarity: Int,
    @SerializedName("class") val classHero: Int, // Chave é "class" no banco
    @SerializedName("hyper_attack") val hyperAttack: Int,
    @SerializedName("base_atk") val baseAtk: Int,
    @SerializedName("base_hp") val baseHp: Int,
    @SerializedName("base_def") val baseDef: Int
)

// 3. Os detalhes do Herói do Usuário (Mapeia as colunas da tabela 'UserHero')
data class UserHeroDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("hero") val hero: Int,
    @SerializedName("user") val user: Int,

    @SerializedName("current_hp") val currentHp: Int,
    @SerializedName("max_hp") val maxHp: Int,

    // Único campo que pode ser nulo na sua tabela UserHero
    @SerializedName("drawback") val drawback: Int?,

    @SerializedName("alive") val alive: Boolean,
    @SerializedName("level") val level: Int,
    @SerializedName("fragments") val fragments: Int,
    @SerializedName("active") val active: Boolean
)