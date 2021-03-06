package com.rob.gab.appokemon.data.network

import androidx.paging.PagingSource
import com.rob.gab.appokemon.Constants.PAGE_LIMIT
import com.rob.gab.appokemon.data.domain.map.mapPokemonResponseToDomain
import com.rob.gab.appokemon.data.domain.model.PokemonModel
import retrofit2.HttpException
import java.io.IOException


class PokemonPagingSource(
    private val service: ApiService
) : PagingSource<Int, PokemonModel>() {

    companion object {
        const val STARTING_POKEMON_ID = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonModel> {
        val position = params.key?: STARTING_POKEMON_ID

        return try {
            val response = service.getPokemons(position*PAGE_LIMIT, params.loadSize)
            val pokemons = mapPokemonResponseToDomain(response)

            LoadResult.Page(
                data = pokemons,
                prevKey = if (position == STARTING_POKEMON_ID) null else position - 1,
                nextKey = if (pokemons.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}