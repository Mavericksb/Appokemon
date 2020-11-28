package com.rob.gab.appokemon.remote

import androidx.paging.PagingData
import com.rob.gab.appokemon.remote.dto.PokemonResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemons(@Query("offset") offset: Int, @Query("limit") limit: Int): PokemonResponse

//    @GET("pokemon/{id}")
//    fun getPokemonDetails(@Path("id") id: Int): PokemonDetailsResponse

}