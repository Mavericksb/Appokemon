package com.rob.gab.appokemon.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.rob.gab.appokemon.R
import com.rob.gab.appokemon.ui.home.HomeViewModel
import com.rob.gab.appokemon.ui.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val homeViewModel: HomeViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_animation)
        pokeballSpinner.startAnimation(animation)

        Handler().postDelayed(
            {
                initApp()
            },
            3000 // La Launch Screen rimarrà visibile per 3 secondi
        )
    }

    private fun initApp() {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
    }
}