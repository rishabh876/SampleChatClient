package com.rishabh.chatclient.chatActivity.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.rishabh.chatclient.R
import com.rishabh.chatclient.chatActivity.presenter.ChatActivityPresenter
import com.rishabh.chatclient.core.BaseActivity
import com.rishabh.chatclient.model.ChatMessage
import io.reactivex.Observable


class ChatActivity : BaseActivity<ChatActivityPresenter.View, ChatActivityPresenter>(), ChatActivityPresenter.View {

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.chat_edittext)
    lateinit var chatEditText: EditText
    @BindView(R.id.send_button)
    lateinit var sendButton: TextView
    @BindView(R.id.progress_bar)
    lateinit var progressBar: ProgressBar

    private val adapter: ChatAdapter by lazy { ChatAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        ButterKnife.bind(this)
        initChatView()
        getPresenter().onViewBinded()
    }

    private fun initChatView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun textChangeObservable() = RxTextView.afterTextChangeEvents(chatEditText).map { it.editable().toString() }

    override fun sendButtonClickObservable(): Observable<String> {
        return RxView.clicks(sendButton).map { chatEditText.text.toString() }
                .mergeWith(Observable.create {
                    chatEditText.setOnEditorActionListener { v, actionId, event ->
                        var consumed = false
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            it.onNext(chatEditText.text.toString())
                            consumed = true
                        }
                        consumed
                    }
                })
    }

    override fun onReplyReceived(chatMessage: ChatMessage) = adapter.addMessage(chatMessage)

    override fun onSentMessage(chatMessage: ChatMessage) = adapter.addMessage(chatMessage)

    override fun addHistory(chatHistory: List<ChatMessage>?) = adapter.addAllMessages(chatHistory)

    override fun clearChatBox() = chatEditText.setText("")

    override fun disableSendButton() = sendButton.setClickable(false)

    override fun enableSendButton() = sendButton.setClickable(true)

    override fun showSendProgress() = progressBar.setVisibility(View.VISIBLE)

    override fun hideSendProgress() = progressBar.setVisibility(View.GONE)

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
