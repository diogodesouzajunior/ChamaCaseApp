package com.diogo.chamacaseapp.ui.base

import androidx.annotation.CallSuper


/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the [_view] that
 * can be accessed from the children classes by calling [view].
 */
abstract class BasePresenter<T : MvpView>: Presenter<T> {

    private var _view: T? = null

    val view: T
        get() { return _view ?: throw MvpViewNotAttachedException() }

    override fun attachView(view: T) {
        _view = view
    }

    @CallSuper
    override fun detachView() {
        unsubscribe()
        _view = null
    }

    open fun unsubscribe(){}

    class MvpViewNotAttachedException : RuntimeException(
            "Please call Presenter.attachView(MvpView) before requesting data to the Presenter")
}
