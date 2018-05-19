package com.rishabh.chatclient.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.rishabh.chatclient.model.ChatMessage
import com.rishabh.chatclient.repository.disk.ChatHistoryDao

@Database(entities = [ChatMessage::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatHistoryDao(): ChatHistoryDao
}