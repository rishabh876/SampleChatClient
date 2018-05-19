package com.rishabh.chatclient.chatActivity.presenter

import com.rishabh.chatclient.core.BasePresenter
import com.rishabh.chatclient.core.BaseView
import com.rishabh.chatclient.repository.network.RestService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChatActivityPresenter @Inject constructor(private val restService: RestService)
    : BasePresenter<ChatActivityPresenter.View>() {

    fun onSendClicked(message: String) {
        ifViewAttached { it.showSendProgress() }
        restService.getResponse(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {
                    val error = it
                    ifViewAttached { it.showError(error.message) }
                })
    }

    interface View : BaseView {
        fun showSendProgress()
        fun hideSendProgress()
        fun showError(message: String?)
    }
}