package com.rishabh.chatclient.repository.disk

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.rishabh.chatclient.model.ChatMessage
import io.reactivex.Single

@Dao
interface ChatHistoryDao {
    @get:Query("SELECT * FROM ${DatabaseConstants.CHATS_TABLE_NAME}")
    val all: Single<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chatMessage: ChatMessage)
}