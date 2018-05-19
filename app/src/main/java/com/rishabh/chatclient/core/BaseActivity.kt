package com.rishabh.chatclient.core

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<V : BaseView, P : BasePresenter<V>> :
        MvpActivity<V, P>(), BaseView, HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mPresenter: P

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun createPresenter(): P = mPresenter

    @CallSuper
    override fun onStart() {
        super.onStart()
        getPresenter().onStart()
    }

    @CallSuper
    override fun onStop() {
        getPresenter().onStop()
        super.onStop()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}