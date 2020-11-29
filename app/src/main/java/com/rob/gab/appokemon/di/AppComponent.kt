package com.rob.gab.appokemon.di

import androidx.paging.ExperimentalPagingApi
import com.rob.gab.appokemon.ui.home.HomeViewModel
import com.rob.gab.appokemon.usecase.GetPokemons
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * App Components
 */
val appModule = module {
    // ViewModels
    viewModel { HomeViewModel(get()) }
//    viewModel { SplashViewModel(get()) }
//    viewModel { WeatherListViewModel(get(), get()) }

    // Use cases
    factory { GetPokemons(get()) }
//    factory { GetWeatherForGivenLocation(get()) }
//    factory { GetCurrentWeather(get()) }
//    factory { LoadPokemons(get()) }

    // Data Repository
//    single<WeatherEntityRepository> { WeatherEntityRepositoryImpl(get()) }
}

// Gather all app modules
@ExperimentalPagingApi val onlinePokemonApp = appModule + roomDatabase + networkProvider
//val offlineWeatherApp = onlineWeatherApp + mockWebServiceModule
//val offlineDBPokemonApp = onlinePokemonApp + roomDatabaseModule