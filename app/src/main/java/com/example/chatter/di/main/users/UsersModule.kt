package com.example.chatter.di.main.users

import com.bumptech.glide.RequestManager
import com.example.chatter.model.User
import com.example.chatter.repository.DatabaseRepository
import com.example.chatter.ui.main.users.UsersRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.Module
import dagger.Provides

@Module
class UsersModule {

    @Provides
    fun provideOption(databaseRepository: DatabaseRepository): FirestoreRecyclerOptions<User> {
        return databaseRepository.getUserList()
    }

    @Provides
    fun provideUsersRecyclerAdapter(
        firestoreRecyclerOptions: FirestoreRecyclerOptions<User>,
        requestManager: RequestManager
    ): UsersRecyclerAdapter {
        return UsersRecyclerAdapter(firestoreRecyclerOptions, requestManager)
    }
}