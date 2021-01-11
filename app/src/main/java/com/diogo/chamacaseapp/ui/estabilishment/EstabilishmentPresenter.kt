package com.diogo.chamacaseapp.ui.estabilishment

import com.diogo.chamacaseapp.data.DataManager
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscribeBy
import rx.schedulers.Schedulers
import timber.log.Timber


class EstabilishmentPresenter(private val dataManager: DataManager) :
    EstabilishmentContract.Presenter() {

    private var subscription: Subscription? = null

    override fun detachView() {
        super.detachView()
        subscription?.unsubscribe()
    }

    override fun searchEstabilishments(searchType: String, location: String, apiKey: String) {
        view.showProgress(true)

        subscription?.unsubscribe()
        subscription = dataManager.searchEstabilishments(searchType, location, apiKey)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    view.showProgress(false)
                    if (it != null && it.results.isNotEmpty()) {
                        view.setResults(it.results)
                    }
                },
                onError = {
                    view.showProgress(false)
                    Timber.e(it)
                }
            )
    }


}