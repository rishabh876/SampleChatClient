package com.rishabh.chatclient.chatActivity.module

import com.rishabh.chatclient.chatActivity.presenter.ChatActivityPresenter
import com.rishabh.chatclient.core.BasePresenter
import dagger.Binds
import dagger.Module

@Module
abstract class ChatModule {
    @Binds
    internal abstract fun presenter(chatPresenter: ChatActivityPresenter): BasePresenter<*>
}