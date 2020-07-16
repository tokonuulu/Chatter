package com.example.chatter.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.chatter.R
import com.example.chatter.ui.login.LoginActivity
import com.example.chatter.ui.main.MainActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity() {

    @Inject lateinit var modelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        viewModel = ViewModelProvider(this, modelProvider)
            .get(SplashScreenViewModel::class.java)

        routeToAppropriateActivity()
    }

    private fun routeToAppropriateActivity() {
        val intent= when (viewModel.getCurrentUid()) {
            null -> Intent(this, LoginActivity::class.java)
            else -> Intent(this, MainActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}