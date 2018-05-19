package com.rishabh.chatclient.chatActivity.presenter

import com.rishabh.chatclient.core.BasePresenter
import com.rishabh.chatclient.core.BaseView
import javax.inject.Inject

class ChatActivityPresenter @Inject constructor() : BasePresenter<ChatActivityPresenter.View>() {

    interface View : BaseView {

    }
}