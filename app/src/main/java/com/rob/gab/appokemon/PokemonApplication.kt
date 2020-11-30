package com.rob.gab.appokemon

import android.app.Application
import android.content.Context
import androidx.paging.ExperimentalPagingApi
//import com.rob.gab.appokemon.di.offlineDBPokemonApp
import com.rob.gab.appokemon.di.onlinePokemonApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PokemonApplication : Application() {

    companion object {
        private lateinit var context: Context
        val applicationContext
            get() = context

    }
    @ExperimentalPagingApi
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PokemonApplication)
            androidFileProperties()
            modules(onlinePokemonApp)
        }


    }
}