package com.rob.gab.appokemon

import android.app.Application
//import com.rob.gab.appokemon.di.offlineDBPokemonApp
import com.rob.gab.appokemon.di.onlinePokemonApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PokemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@PokemonApplication)
            androidFileProperties()
            modules(onlinePokemonApp)
        }
    }
}