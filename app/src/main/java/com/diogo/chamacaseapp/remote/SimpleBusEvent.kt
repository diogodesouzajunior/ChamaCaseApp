package com.diogo.chamacaseapp.data.remote

import rx.Observable
import rx.subjects.PublishSubject


@Suppress("UNCHECKED_CAST")
class SimpleBusEvent(var mBusSubject: PublishSubject<Any> = PublishSubject.create() ){

    fun post(event: Any) {
        mBusSubject.onNext(event)
    }

    fun observable(): Observable<Any> {
        return mBusSubject
    }

    fun <T> filteredObservable(eventClass: Class<T>): Observable<T> {
        return mBusSubject
                .filter { event -> eventClass.isInstance(event) }
                .map { event -> event as T }
                .distinctUntilChanged()
    }
}