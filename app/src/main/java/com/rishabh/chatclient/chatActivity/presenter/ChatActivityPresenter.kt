package com.rishabh.chatclient.chatActivity.presenter

import com.rishabh.chatclient.core.BasePresenter
import com.rishabh.chatclient.core.BaseView
import com.rishabh.chatclient.model.ChatMessage
import com.rishabh.chatclient.model.ChatResponse
import com.rishabh.chatclient.repository.disk.ChatHistoryDao
import com.rishabh.chatclient.repository.network.RestService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChatActivityPresenter @Inject constructor(private val restService: RestService,
                                                private val chatHistoryDao: ChatHistoryDao)
    : BasePresenter<ChatActivityPresenter.View>() {

    private var notSendMessage: String? = null

    fun onViewBinded() {
        fetchHistory()
    }

    private fun fetchHistory() {
        chatHistoryDao.all
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    ifViewAttached {
                        it.disableSendButton()
                        it.showSendProgress()
                    }
                }
                .doOnEvent { t1, t2 ->
                    ifViewAttached {
                        it.enableSendButton()
                        it.hideSendProgress()
                    }
                }
                .subscribe({
                    val chatHistory = it
                    ifViewAttached { it.addHistory(chatHistory) }
                    checkIfLastMessageNotSent(chatHistory)
                }, {}).let { compositeDisposable.add(it) }
    }

    private fun checkIfLastMessageNotSent(chatHistory: List<ChatMessage>?) {
        val lastMessageSentByMe = chatHistory?.takeIf { it.isNotEmpty() }?.last()?.sentByMe
        if (lastMessageSentByMe == true) {
            sendMessage(chatHistory.last().message)
        }
    }

    override fun onStart() {
        super.onStart()
        ifViewAttached {
            it.textChangeObservable()
                    .subscribe { onTextTyped(it) }
                    .let { viewDisposables.add(it) }
            it.sendButtonClickObservable()
                    .subscribe { onSendClicked(it) }
                    .let { viewDisposables.add(it) }
        }
    }

    private fun onSendClicked(message: String) {
        ifViewAttached {
            it.showSendProgress()
            it.clearChatBox()
            addSendMessageToList(message)
            it.disableSendButton()
        }
        sendMessage(message)
    }

    private fun sendMessage(message: String) {
        notSendMessage = null
        restService.getResponse(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { t1, t2 ->
                    ifViewAttached {
                        it.hideSendProgress()
                        it.enableSendButton()
                    }
                }
                .subscribe({
                    onReplyReceived(it)
                }, {
                    onSendError(message, it)
                })
                .let { compositeDisposable.add(it) }
    }

    private fun onSendError(message: String, error: Throwable?) {
        notSendMessage = message
        ifViewAttached {
            it.showSendError(error?.message ?: "Something went wrong")
        }
    }

    private fun addSendMessageToList(message: String) {
        val chatMessage = ChatMessage(true, message, System.currentTimeMillis())
        Completable.fromAction { chatHistoryDao.insert(chatMessage) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        ifViewAttached { it.onSentMessage(chatMessage) }
    }

    private fun onReplyReceived(it: ChatResponse?) {
        val chatMessage = ChatMessage(false, it?.message?.message ?: "", System.currentTimeMillis())
        Completable.fromAction { chatHistoryDao.insert(chatMessage) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        ifViewAttached { it.onReplyReceived(chatMessage) }
    }

    private fun onTextTyped(typedMessage: String?) {
        if (typedMessage.isNullOrBlank()) {
            ifViewAttached { it.disableSendButton() }
        } else {
            ifViewAttached { it.enableSendButton() }
        }
    }

    fun onNetworkConnected() {
        notSendMessage?.let {
            ifViewAttached {
                it.showSendProgress()
                it.disableSendButton()
            }
            sendMessage(it)
        }
    }

    interface View : BaseView {
        fun showSendProgress()
        fun hideSendProgress()
        fun onReplyReceived(chatMessage: ChatMessage)
        fun showSendError(message: String?)
        fun disableSendButton()
        fun enableSendButton()
        fun textChangeObservable(): Observable<String?>
        fun sendButtonClickObservable(): Observable<String>
        fun onSentMessage(chatMessage: ChatMessage)
        fun clearChatBox()
        fun addHistory(chatHistory: List<ChatMessage>?)
    }
}