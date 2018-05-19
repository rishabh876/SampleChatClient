package com.rishabh.chatclient.dagger

import android.content.Context
import com.rishabh.chatclient.app.ChatClientApp
import com.rishabh.chatclient.repository.network.NetworkConstants
import com.rishabh.chatclient.repository.network.RestService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module()
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: ChatClientApp): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideRestService(retrofit: Retrofit): RestService {
        return retrofit.create(RestService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstants.API_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(application: ChatClientApp): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(application.cacheDir, cacheSize.toLong())

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient().newBuilder()
                .cache(cache)
                .addInterceptor(logging)
                .build()
    }
}