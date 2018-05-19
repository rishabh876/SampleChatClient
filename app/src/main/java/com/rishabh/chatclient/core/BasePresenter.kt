package com.rishabh.chatclient.core

import android.support.annotation.CallSuper
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V : BaseView> : MvpBasePresenter<V>() {
    var compositeDisposable = CompositeDisposable()
    var viewDisposables = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    override fun destroy() {
        super.destroy()
        compositeDisposable.dispose()
    }

    @CallSuper
    open fun onStart() {}

    @CallSuper
    open fun onStop() {
        viewDisposables.clear()
    }
}