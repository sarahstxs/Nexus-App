package com.example.nexusappxml.data.network

import com.example.nexusappxml.data.model.BattleResolveResponse
import com.example.nexusappxml.data.model.DeckResponse
import com.example.nexusappxml.data.model.HeroCompleteResponse
import com.example.nexusappxml.data.model.HeroResponse
import com.example.nexusappxml.data.model.LoginRequest
import com.example.nexusappxml.data.model.PackResponse
import com.example.nexusappxml.data.model.RankResponse
import com.example.nexusappxml.data.model.RegisterRequest
import com.example.nexusappxml.data.model.RegisterResponse
import com.example.nexusappxml.data.model.UserResponse
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

// Modelo de dados que recebe a resposta do token do FastAPI
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("user_id") val userId: Int
)

interface ApiService {
    @POST("/users/login") // Rota exata na FastAPI
    suspend fun login(
        @Body request: LoginRequest
    ): TokenResponse

    @POST("/users/")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("/users/list/{id_user}")
    suspend fun getUserDetails(
        @Path("id_user") id: Int
    ): Response<UserResponse>

    @GET("/heroes/list-all-heroes") // Confirme se a rota no Python é exatamente esta
    suspend fun getHeroes(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<HeroResponse>

    @GET("/user-heroes/list-all-user-heroes") // Confirme se a rota no Python é exatamente esta
    suspend fun getUserHeroes(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("id_user") id_user: Int
    ): Response<HeroResponse>

        // Aqui você coloca SÓ a rota base. O Retrofit monta os ? e & sozinho!
    @GET("/heroes/list-complete-user-hero/{id_hero}/{id_user}")
    suspend fun getHeroComplete(
            @Path("id_hero") heroId: Int,
            @Path("id_user") userId: Int
    ): Response<HeroCompleteResponse>

    @GET("/packs/list-active") // Ajuste o caminho caso tenha um prefixo como "pack/list-active"
    suspend fun getActivePacks(): Response<PackResponse>

    // Nova rota para comprar o pack
    @GET("/users/comprar-pack/{id_user}/{id_pack}")
    suspend fun buyPack(
        @Path("id_user") userId: Int,
        @Path("id_pack") packId: Int
    ): Response<JsonElement>

    @GET("/users/show-rank")
    suspend fun getRank(): Response<RankResponse>

    @GET("/decks/list-user-decks")
    suspend fun listUserDecks(): Response<List<DeckResponse>>

    @POST("/decks/save-deck")
    suspend fun saveDeck(
        @Query("id_hero1") h1: Int,
        @Query("id_hero2") h2: Int,
        @Query("id_hero3") h3: Int,
        @Query("id_hero4") h4: Int,
        @Query("id_hero5") h5: Int,
        @Query("id_hero6") h6: Int
    ): Response<JsonElement>

    // Dentro da sua interface ApiService:
    @POST("/battles/start")
    suspend fun resolveBattle(
        @Query("floor") floor: Int,
        @Query("place_id") placeId: Int,
        @Query("hero1") h1: Int,
        @Query("hero2") h2: Int,
        @Query("hero3") h3: Int,
        @Query("hero4") h4: Int,
        @Query("hero5") h5: Int,
        @Query("hero6") h6: Int
    ): Response<BattleResolveResponse>
}



