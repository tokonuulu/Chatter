package com.example.chatter

import com.example.chatter.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class ChatterApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}