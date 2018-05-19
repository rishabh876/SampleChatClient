package com.rishabh.chatclient.chatActivity.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.rishabh.chatclient.R
import com.rishabh.chatclient.chatActivity.presenter.ChatActivityPresenter
import com.rishabh.chatclient.core.BaseActivity


class ChatActivity : BaseActivity<ChatActivityPresenter.View, ChatActivityPresenter>(), ChatActivityPresenter.View {

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView
    private val adapter: ChatAdapter by lazy { ChatAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        ButterKnife.bind(this)
        initChatView()
    }

    private fun initChatView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun showSendProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideSendProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
