package com.example.chatter

import android.app.Application
import com.example.chatter.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject



class ChatterApplication : Application(), HasAndroidInjector {

    @Inject lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.create()
            .injectApplication(this)

    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}