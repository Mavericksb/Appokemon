package com.rob.gab.appokemon.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * App Components
 */
val appModule = module {
    // ViewModels
//    viewModel { (id: DailyForecastId) -> DetailViewModel(id, get()) }
//    viewModel { SplashViewModel(get()) }
//    viewModel { WeatherListViewModel(get(), get()) }

    // Use cases
//    factory { GetWeatherDetail(get()) }
//    factory { GetWeatherForGivenLocation(get()) }
//    factory { GetCurrentWeather(get()) }
//    factory { LoadCurrentWeather(get()) }

    // Data Repository
//    single<WeatherEntityRepository> { WeatherEntityRepositoryImpl(get()) }
}

// Gather all app modules
val onlinePokemonApp = appModule // + remoteNetworkModule
//val offlineWeatherApp = onlineWeatherApp + mockWebServiceModule
//val offlineDBPokemonApp = onlinePokemonApp + roomDatabaseModule