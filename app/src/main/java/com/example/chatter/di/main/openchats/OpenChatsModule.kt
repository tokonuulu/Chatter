package com.example.chatter.di.main.openchats

import com.bumptech.glide.RequestManager
import com.example.chatter.model.OpenChat
import com.example.chatter.repository.AuthRepository
import com.example.chatter.repository.DatabaseRepository
import com.example.chatter.ui.main.openchats.OpenChatsFragment
import com.example.chatter.ui.main.openchats.OpenChatsRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [OpenChatsModule.OpenChatsFragmentModule::class]
)
class OpenChatsModule {

    @Provides
    fun provideOption(databaseRepository: DatabaseRepository): FirestoreRecyclerOptions<OpenChat> {
        return databaseRepository.getOpenChatsList()
    }

    @Provides
    fun provideOpenChatsRecyclerAdapter(
        firestoreRecyclerOptions: FirestoreRecyclerOptions<OpenChat>,
        requestManager: RequestManager,
        authRepository: AuthRepository
    ): OpenChatsRecyclerAdapter {
        return OpenChatsRecyclerAdapter(firestoreRecyclerOptions, requestManager, authRepository.getCurrentUid()!!)
    }

    @Module
    abstract class OpenChatsFragmentModule {
        @ContributesAndroidInjector
        abstract fun contributeOpenChatsFragment(): OpenChatsFragment
    }

}