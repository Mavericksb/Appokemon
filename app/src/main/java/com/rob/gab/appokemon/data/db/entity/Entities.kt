package com.rob.gab.appokemon.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemons_table")
class Entities (
    @PrimaryKey val id: Int,
            val name: String
)