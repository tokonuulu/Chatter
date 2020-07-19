package com.example.chatter.di

import com.example.chatter.di.main.openchats.OpenChatsModule
import com.example.chatter.di.main.users.UsersModule
import com.example.chatter.di.message.MessagesModule
import com.example.chatter.ui.login.LoginActivity
import com.example.chatter.ui.main.MainActivity
import com.example.chatter.ui.messages.MessagesActivity
import com.example.chatter.ui.profile.ProfileActivity
import com.example.chatter.ui.register.RegisterActivity
import com.example.chatter.ui.splash.SplashScreenActivity
import com.example.chatter.ui.user.OtherUserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeRegisterActivity(): RegisterActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(
        modules = [UsersModule::class,
            OpenChatsModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeProfileActivity(): ProfileActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashScreenActivity

    @ContributesAndroidInjector
    abstract fun contributeOtherUserActivity(): OtherUserActivity

    @ContributesAndroidInjector(
        modules = [MessagesModule::class]
    )
    abstract fun contributeMessagesActivity(): MessagesActivity
}