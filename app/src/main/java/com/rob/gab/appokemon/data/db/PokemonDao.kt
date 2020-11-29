package com.rob.gab.appokemon.data.db

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rob.gab.appokemon.domain.model.PokemonModel

interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonModel>)

    @Query( "SELECT * FROM pokemons_table " +
            "WHERE id > :offset AND id < limit" +
    "ORDER BY id ASC")
    fun getPokemons(offset: Int, limit: Int): PagingSource<Int, PokemonModel>

    @Query( "DELETE FROM pokemons_table")
    suspend fun clearPokemons()
}