package com.rishabh.chatclient.dagger

import com.rishabh.chatclient.app.ChatClientApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class])
interface AppComponent : AndroidInjector<ChatClientApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ChatClientApp>()
}