package com.rob.gab.appokemon.data.db.dao

import androidx.room.*
import com.rob.gab.appokemon.data.db.map.Converters
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "pokemons_table")
data class EntityPokemon(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(tableName = "pokemon_details_table")
@TypeConverters(Converters::class)
data class EntityPokemonDetails(
    @PrimaryKey val id: Int,
    var name: String? = null,
    var height: Double? = null,
    var weight: Double? = null,
    var types: List<Type>? = null,
    var stats: List<Stats>? = null
) {
    data class Type(
        var name: String? = null,
        var url: String? = null
    )

    data class Stats(
        var base: Int? = null,
        var effort: Int? = null,
        var name: String? = null,
        var url: String? = null
    )
}