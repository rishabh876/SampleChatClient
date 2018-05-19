package com.rishabh.chatclient.chatActivity.view

import android.os.Bundle
import com.rishabh.chatclient.R
import com.rishabh.chatclient.chatActivity.presenter.ChatActivityPresenter
import com.rishabh.chatclient.core.BaseActivity

class ChatActivity : BaseActivity<ChatActivityPresenter.View, ChatActivityPresenter>(), ChatActivityPresenter.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
