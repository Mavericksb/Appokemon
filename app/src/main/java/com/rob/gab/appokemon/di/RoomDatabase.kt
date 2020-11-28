package com.rob.gab.appokemon.di

import com.rob.gab.appokemon.repository.PokemonRepository
import com.rob.gab.appokemon.repository.PokemonRepositoryImpl
import org.koin.dsl.module

val roomDatabase = module {
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
}