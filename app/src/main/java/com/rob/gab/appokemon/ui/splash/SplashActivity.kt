package com.rob.gab.appokemon.ui.splash

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.rob.gab.appokemon.R
import io.uniflow.androidx.flow.onStates
import io.uniflow.core.flow.data.UIState

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onStates(splashViewModel) { state ->
            when (state) {
                is UIState.Loading -> showIsLoading()
                is UIState.Success -> showIsLoaded()
                is UIState.Failed -> showError(state.error)
            }
        }
        splashViewModel.getLastWeather()
    }

    fun showIsLoading() {
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_animation)
    }

    fun showIsLoaded() {}

    fun showError(error: Throwable?) {
    }
}