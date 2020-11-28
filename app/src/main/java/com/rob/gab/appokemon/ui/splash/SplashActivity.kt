package com.rob.gab.appokemon.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.ui.home.HomeViewModel
import com.rob.gab.appokemon.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val homeViewModel: HomeViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(
            {
                initApp()
            },
            3000 // La Launch Screen rimarrà visibile per 3 secondi
        )

//        onEvents(splashViewModel) {event ->
//            when(event.take()){
//                is UIEvent.Loading -> showIsLoading()
//            }
//        }
//        onStates(splashViewModel) { state ->
//            when (state) {
//                is UIState.Success -> showIsLoaded()
//                is UIState.Failed -> showError(state.error)
//            }
//        }
//
//        observeStates()
//
//        splashViewModel.userIntent.offer(SplashIntent.FetchPokemons)
    }

    private fun initApp() {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
    }

//    private fun observeStates() {
//        lifecycleScope.launch {
//            splashViewModel.state.collect {
//                when(it){
//                    SplashState.Loading -> showIsLoading()
//                }
//            }
//        }
//    }
//
//    fun showIsLoading() {
//        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_animation)
//        pokeballSpinner.startAnimation(animation)
//    }
//
//    fun showIsLoaded() {
//
//    }
//
//    fun showError(error: Throwable?) {
//    }
}