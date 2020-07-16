package com.example.chatter.ui.splash

import androidx.lifecycle.ViewModel
import com.example.chatter.repository.AuthRepository
import javax.inject.Inject

class SplashScreenViewModel
@Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getCurrentUid() : String? {
        return authRepository.getCurrentUid()
    }
}