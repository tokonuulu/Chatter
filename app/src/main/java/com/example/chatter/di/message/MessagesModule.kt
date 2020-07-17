package com.example.chatter.di.message

import com.bumptech.glide.RequestManager
import com.example.chatter.ui.messages.MessagesRecyclerAdapter
import dagger.Module
import dagger.Provides

@Module
class MessagesModule {

    @Provides
    fun provideMessagesRecyclerAdapter(requestManager: RequestManager) : MessagesRecyclerAdapter {
        return MessagesRecyclerAdapter(requestManager)
    }

}