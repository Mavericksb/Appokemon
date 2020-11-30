package com.rob.gab.appokemon.data.network

import com.rob.gab.appokemon.data.network.dto.PokemonDetailsResponse
import com.rob.gab.appokemon.data.network.dto.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemons(@Query("offset") offset: Int, @Query("limit") limit: Int): PokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): PokemonDetailsResponse?

}