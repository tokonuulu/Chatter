package com.example.chatter.di

import com.example.chatter.ui.login.LoginActivity
import com.example.chatter.ui.main.MainActivity
import com.example.chatter.ui.main.MainViewModel
import com.example.chatter.ui.profile.ProfileActivity
import com.example.chatter.ui.register.RegisterActivity
import com.example.chatter.ui.splash.SplashScreenActivity
import com.example.chatter.ui.splash.SplashScreenViewModel
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeRegisterActivity() : RegisterActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity() : LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : MainActivity

    @ContributesAndroidInjector
    abstract fun contributeProfileActivity() : ProfileActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity() : SplashScreenActivity
}