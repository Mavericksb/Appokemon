package com.rob.gab.appokemon.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bumptech.glide.load.HttpException
import com.rob.gab.appokemon.Constants.PAGE_LIMIT
import com.rob.gab.appokemon.data.db.PokemonDatabase
import com.rob.gab.appokemon.data.db.map.mapPokemon
import com.rob.gab.appokemon.domain.model.PokemonModel
import java.io.IOException


@ExperimentalPagingApi
class PokemonsRemoteMediator(
    private val service: ApiService,
    private val database: PokemonDatabase
): RemoteMediator<Int, PokemonModel>() {

    companion object {
        const val STARTING_POKEMON_ID = 0
    }


    override suspend fun load(loadType: LoadType, state: PagingState<Int, PokemonModel>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                0
            }
            LoadType.PREPEND -> {
                state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()?.id.let {
                   if(it==0) 0 else it?.minus(1)?: 0
                }
            }
            LoadType.APPEND -> {
                state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.id?.plus(1) ?: 0
            }
            }


        try {
            val response = service.getPokemons(page*PAGE_LIMIT, state.config.pageSize)
            val pokemons = mapPokemon(response)

            val endOfPaginationReached = pokemons.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.pokemonDao().clearPokemons()
                }
                val prevKey = if (page == STARTING_POKEMON_ID) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                database.pokemonDao().insertAll(pokemons)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}