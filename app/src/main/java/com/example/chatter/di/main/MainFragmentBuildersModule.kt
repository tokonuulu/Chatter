package com.example.chatter.di.main

import com.example.chatter.di.main.users.UsersModule
import com.example.chatter.ui.main.users.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector(modules = [UsersModule::class])
    abstract fun contributeUsersFragment() : UserFragment

}