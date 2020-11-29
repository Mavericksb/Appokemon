package com.rob.gab.appokemon.di

import androidx.paging.ExperimentalPagingApi
import com.rob.gab.appokemon.data.db.PokemonDatabase
import com.rob.gab.appokemon.repository.PokemonRepository
import com.rob.gab.appokemon.repository.PokemonRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@ExperimentalPagingApi val roomDatabase = module {

    single { PokemonDatabase.getInstance(androidContext()) }

    single<PokemonRepository> { PokemonRepositoryImpl(get(), get(), get()) }


}