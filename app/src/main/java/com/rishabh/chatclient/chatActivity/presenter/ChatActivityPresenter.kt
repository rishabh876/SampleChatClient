package com.rishabh.chatclient.chatActivity.presenter

import com.rishabh.chatclient.core.BasePresenter
import com.rishabh.chatclient.core.BaseView
import com.rishabh.chatclient.model.ChatMessage
import com.rishabh.chatclient.model.ChatResponse
import com.rishabh.chatclient.repository.network.RestService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChatActivityPresenter @Inject constructor(private val restService: RestService)
    : BasePresenter<ChatActivityPresenter.View>() {

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
                    onSendError(it)
                })
                .let { compositeDisposable.add(it) }
    }

    private fun onSendError(error: Throwable?) {
        ifViewAttached {
            it.showError(error?.message ?: "Something went wrong")
        }
    }

    private fun addSendMessageToList(message: String) {
        val chatMessage = ChatMessage(true, message, System.currentTimeMillis())
        ifViewAttached { it.onSentMessage(chatMessage) }
    }

    private fun onReplyReceived(it: ChatResponse?) {
        val chatMessage = ChatMessage(false, it?.message?.message ?: "", System.currentTimeMillis())
        ifViewAttached {
            it.onReplyReceived(chatMessage)
        }
    }

    private fun onTextTyped(typedMessage: String?) {
        if (typedMessage.isNullOrBlank()) {
            ifViewAttached { it.disableSendButton() }
        } else {
            ifViewAttached { it.enableSendButton() }
        }
    }

    interface View : BaseView {
        fun showSendProgress()
        fun hideSendProgress()
        fun onReplyReceived(chatMessage: ChatMessage)
        fun showError(message: String?)
        fun disableSendButton()
        fun enableSendButton()
        fun textChangeObservable(): Observable<String?>
        fun sendButtonClickObservable(): Observable<String>
        fun onSentMessage(chatMessage: ChatMessage)
        fun clearChatBox()
    }
}