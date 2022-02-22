package com.ginzburgworks.filmfinder

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

private const val NOT_IMPLEMENTED_ERROR_MSG = "must bind AutoDisposable to a Lifecycle first"

class AutoDisposable : LifecycleObserver, LifecycleEventObserver {

    lateinit var compositeDisposable: CompositeDisposable
    fun bindTo(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        compositeDisposable = CompositeDisposable()
    }

    fun add(disposable: Disposable) {
        if (::compositeDisposable.isInitialized) {
            compositeDisposable.add(disposable)
        } else {
            throw NotImplementedError(NOT_IMPLEMENTED_ERROR_MSG)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_STOP) {
            compositeDisposable.dispose()
        }
    }
}

fun Disposable.addTo(autoDisposable: AutoDisposable) {
    autoDisposable.add(this)
}
