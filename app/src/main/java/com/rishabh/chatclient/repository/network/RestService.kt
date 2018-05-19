package com.rishabh.chatclient.repository.network

import com.rishabh.chatclient.model.ChatResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestService {
    @GET("/api/chat/")
    fun getResponse(@Query(NetworkConstants.QueryParams.MESSAGE) message: String,
                    @Query(NetworkConstants.QueryParams.API_KEY) apiKey: String = NetworkConstants.QueryParams.API_KEY_VALUE,
                    @Query(NetworkConstants.QueryParams.CHAT_BOT_ID) chatBotId: String = NetworkConstants.QueryParams.CHAT_BOT_ID_VALUE,
                    @Query(NetworkConstants.QueryParams.EXTERNAL_ID) externalId: String = NetworkConstants.QueryParams.EXTERNAL_ID_VALUE)
            : Single<ChatResponse>
}