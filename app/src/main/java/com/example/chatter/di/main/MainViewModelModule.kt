package com.example.chatter.di.main

import androidx.lifecycle.ViewModel
import com.example.chatter.di.ViewModelKey
import com.example.chatter.ui.main.users.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUsersViewModel(viewModel: UserViewModel): ViewModel

}