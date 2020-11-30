package com.rob.gab.appokemon.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bumptech.glide.load.HttpException
import com.rob.gab.appokemon.data.db.PokemonDatabase
import com.rob.gab.appokemon.data.db.dao.EntityPokemon
import com.rob.gab.appokemon.data.db.map.mapResponseToEntity
import java.io.IOException


@ExperimentalPagingApi
class PokemonsRemoteMediator(
    private val service: ApiService,
    private val database: PokemonDatabase
) : RemoteMediator<Int, EntityPokemon>() {

    companion object {
        const val STARTING_POKEMON_ID = 0
    }


    override suspend fun load(loadType: LoadType, state: PagingState<Int, EntityPokemon>): MediatorResult {

        val position = when (loadType) {
            LoadType.REFRESH -> {
                0
            }
            LoadType.PREPEND -> {
                state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()?.id.let {
                    val id = it?.minus(1)
                    if (id == 0) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else id?.minus(1) ?: 0
                }
            }
            LoadType.APPEND -> {
                state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.id ?: 0
            }
        }


        try {
            val response = service.getPokemons(position, state.config.pageSize)
            val pokemons = mapResponseToEntity(response)

            val endOfPaginationReached = pokemons.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.pokemonDao().clearPokemons()
                }
//                val prevKey = if (position == STARTING_POKEMON_ID) null else position - 1
//                val nextKey = if (endOfPaginationReached) null else position + 1
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