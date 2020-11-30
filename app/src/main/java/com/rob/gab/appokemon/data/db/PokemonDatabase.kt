package com.rob.gab.appokemon.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rob.gab.appokemon.data.db.dao.EntityPokemon
import com.rob.gab.appokemon.data.db.dao.EntityPokemonDetails
import com.rob.gab.appokemon.data.db.map.Converters

@Database
    (
    entities = [EntityPokemon::class, EntityPokemonDetails::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getInstance(context: Context): PokemonDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext, PokemonDatabase::class.java, "Appokemon.db"
            ).build()

    }
}
