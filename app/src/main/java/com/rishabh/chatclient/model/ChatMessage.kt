package com.rishabh.chatclient.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.rishabh.chatclient.repository.disk.DatabaseConstants

@Entity(tableName = DatabaseConstants.CHATS_TABLE_NAME)
class ChatMessage(val sentByMe: Boolean,
                  val message: String,
                  @PrimaryKey val timeStamp: Long)
