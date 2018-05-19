package com.rishabh.chatclient.model

class ChatResponse(val success: Int,
                   val errorMessage: String,
                   val message: Message? = null)