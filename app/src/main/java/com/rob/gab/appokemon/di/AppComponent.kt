package com.rob.gab.appokemon.di

import androidx.paging.ExperimentalPagingApi
import com.rob.gab.appokemon.ui.details.DetailViewModel
import com.rob.gab.appokemon.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * App Components
 */
val appModule = module {
    // ViewModels
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }

//    // Use cases
//    factory { GetPokemons(get()) }
}

// Gather all app modules
@ExperimentalPagingApi val onlinePokemonApp = appModule + roomDatabase + networkProvider
