package com.rishabh.chatclient.dagger

import com.rishabh.chatclient.chatActivity.module.ChatModule
import com.rishabh.chatclient.chatActivity.view.ChatActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [ChatModule::class])
    abstract fun bindChatActivity(): ChatActivity
}