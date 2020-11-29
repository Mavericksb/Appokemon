package com.rob.gab.appokemon.data.remote

import com.rob.gab.appokemon.data.remote.dto.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemons(@Query("offset") offset: Int, @Query("limit") limit: Int): PokemonResponse

//    @GET("pokemon/{id}")
//    fun getPokemonDetails(@Path("id") id: Int): PokemonDetailsResponse

}