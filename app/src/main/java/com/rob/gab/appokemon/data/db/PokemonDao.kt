package com.rob.gab.appokemon.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rob.gab.appokemon.data.db.dao.EntityPokemon
import com.rob.gab.appokemon.data.db.dao.EntityPokemonDetails
import java.time.OffsetDateTime

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<EntityPokemon>)

    @Query( "SELECT * FROM pokemons_table ORDER BY id ASC")
    fun getPokemons(): PagingSource<Int, EntityPokemon>

    @Query( "SELECT * FROM pokemon_details_table WHERE id = :id")
    suspend fun getPokemonDetails(id: Int): EntityPokemonDetails

    @Query( "SELECT * FROM pokemon_details_table WHERE name LIKE :name")
    suspend fun getPokemonByName(name: String): EntityPokemonDetails

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(pokemonDetails: EntityPokemonDetails?)

    @Query( "DELETE FROM pokemons_table")
    suspend fun clearPokemons()
}