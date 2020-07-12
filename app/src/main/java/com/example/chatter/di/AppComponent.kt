package com.example.chatter.di

import android.app.Application
import com.example.chatter.ChatterApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        AppModule::class,
        ViewModelModule::class]
)
interface AppComponent {

    fun injectApplication(app: ChatterApplication)

}